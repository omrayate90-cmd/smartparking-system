import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(PORT),
            0
        );

        // Website files
        server.createContext("/", WebServer::serveWebsite);

        // API
        server.createContext("/api/stations", WebServer::handleStations);

        server.setExecutor(null);
        server.start();

        System.out.println("--------------------------------");
        System.out.println("Smart Car Parking Web Server");
        System.out.println("--------------------------------");
        System.out.println("Server running at:");
        System.out.println("http://localhost:" + PORT);
        System.out.println("--------------------------------");
    }

    // =========================================================
    // SERVE HTML / CSS / JS
    // =========================================================

    private static void serveWebsite(HttpExchange exchange) {

        try {

            String path = exchange.getRequestURI().getPath();

            if (path.equals("/")) {
                path = "/index.html";
            }

            Path file = Paths.get(
                "frontend" + path
            );

            if (!Files.exists(file) || Files.isDirectory(file)) {

                String response = "404 - File Not Found";

                sendResponse(
                    exchange,
                    404,
                    "text/plain",
                    response
                );

                return;
            }

            String contentType = getContentType(path);

            byte[] data = Files.readAllBytes(file);

            exchange.getResponseHeaders()
                .set("Content-Type", contentType);

            exchange.sendResponseHeaders(
                200,
                data.length
            );

            OutputStream os = exchange.getResponseBody();

            os.write(data);
            os.close();

        } catch (Exception e) {

            e.printStackTrace();

            try {
                sendResponse(
                    exchange,
                    500,
                    "text/plain",
                    "Server Error"
                );
            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // API
    // =========================================================

    private static void handleStations(HttpExchange exchange) {

        try {

            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

                getStations(exchange);

            } else if (
                exchange.getRequestMethod().equalsIgnoreCase("POST")
            ) {

                addStation(exchange);

            } else {

                sendResponse(
                    exchange,
                    405,
                    "text/plain",
                    "Method Not Allowed"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            try {

                sendResponse(
                    exchange,
                    500,
                    "text/plain",
                    e.getMessage()
                );

            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // GET STATIONS
    // =========================================================

    private static void getStations(HttpExchange exchange)
        throws Exception {

        Connection con = DBConnection.getConnection();

        if (con == null) {

            sendResponse(
                exchange,
                500,
                "application/json",
                "{\"error\":\"Database connection failed\"}"
            );

            return;
        }

        String sql =
            "SELECT * FROM charging_stations";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ResultSet rs =
            ps.executeQuery();

        StringBuilder json =
            new StringBuilder("[");

        boolean first = true;

        while (rs.next()) {

            if (!first) {
                json.append(",");
            }

            first = false;

            json.append("{");

            json.append("\"id\":")
                .append(rs.getInt("id"))
                .append(",");

            json.append("\"station_name\":\"")
                .append(escapeJson(
                    rs.getString("station_name")
                ))
                .append("\",");

            json.append("\"location\":\"")
                .append(escapeJson(
                    rs.getString("location")
                ))
                .append("\",");

            json.append("\"charging_slot\":\"")
                .append(escapeJson(
                    rs.getString("charging_slot")
                ))
                .append("\",");

            json.append("\"connector_type\":\"")
                .append(escapeJson(
                    rs.getString("connector_type")
                ))
                .append("\",");

            json.append("\"status\":\"")
                .append(escapeJson(
                    rs.getString("status")
                ))
                .append("\",");

            json.append("\"power_kw\":")
                .append(rs.getDouble("power_kw"))
                .append(",");

            json.append("\"price_per_unit\":")
                .append(rs.getDouble("price_per_unit"));

            json.append("}");
        }

        json.append("]");

        rs.close();
        ps.close();
        con.close();

        sendResponse(
            exchange,
            200,
            "application/json",
            json.toString()
        );
    }

    // =========================================================
    // ADD STATION
    // =========================================================

    private static void addStation(HttpExchange exchange)
        throws Exception {

        InputStream input =
            exchange.getRequestBody();

        String body =
            new String(
                input.readAllBytes(),
                StandardCharsets.UTF_8
            );

        Map<String, String> data =
            parseFormData(body);

        String station =
            data.get("station_name");

        String location =
            data.get("location");

        String slot =
            data.get("charging_slot");

        String connector =
            data.get("connector_type");

        String status =
            data.get("status");

        String power =
            data.get("power_kw");

        String price =
            data.get("price_per_unit");

        String sql =
            "INSERT INTO charging_stations " +
            "(station_name, location, charging_slot, " +
            "connector_type, status, power_kw, price_per_unit) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection con =
            DBConnection.getConnection();

        if (con == null) {

            sendResponse(
                exchange,
                500,
                "application/json",
                "{\"error\":\"Database connection failed\"}"
            );

            return;
        }

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setString(1, station);
        ps.setString(2, location);
        ps.setString(3, slot);
        ps.setString(4, connector);
        ps.setString(5, status);
        ps.setDouble(6, Double.parseDouble(power));
        ps.setDouble(7, Double.parseDouble(price));

        ps.executeUpdate();

        ps.close();
        con.close();

        sendResponse(
            exchange,
            200,
            "application/json",
            "{\"message\":\"Station saved successfully\"}"
        );
    }

    // =========================================================
    // FORM PARSER
    // =========================================================

    private static Map<String, String> parseFormData(
        String body
    ) throws Exception {

        Map<String, String> map =
            new HashMap<>();

        String[] pairs =
            body.split("&");

        for (String pair : pairs) {

            String[] parts =
                pair.split("=", 2);

            if (parts.length == 2) {

                String key =
                    URLDecoder.decode(
                        parts[0],
                        StandardCharsets.UTF_8
                    );

                String value =
                    URLDecoder.decode(
                        parts[1],
                        StandardCharsets.UTF_8
                    );

                map.put(key, value);
            }
        }

        return map;
    }

    // =========================================================
    // CONTENT TYPE
    // =========================================================

    private static String getContentType(String path) {

        if (path.endsWith(".html")) {
            return "text/html";
        }

        if (path.endsWith(".css")) {
            return "text/css";
        }

        if (path.endsWith(".js")) {
            return "application/javascript";
        }

        return "text/plain";
    }

    // =========================================================
    // SEND RESPONSE
    // =========================================================

    private static void sendResponse(
        HttpExchange exchange,
        int status,
        String contentType,
        String response
    ) throws IOException {

        byte[] data =
            response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
            .set("Content-Type", contentType);

        exchange.sendResponseHeaders(
            status,
            data.length
        );

        OutputStream os =
            exchange.getResponseBody();

        os.write(data);
        os.close();
    }

    // =========================================================
    // JSON ESCAPE
    // =========================================================

    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}