let allStations = [];


// ==========================================
// LOAD STATIONS
// ==========================================

async function loadStations() {

    const container =
        document.getElementById("stationContainer");

    container.innerHTML = `
        <div class="loading-card">
            <div class="loader"></div>
            <p>Loading stations...</p>
        </div>
    `;

    try {

        const response =
            await fetch("/api/stations");

        if (!response.ok) {
            throw new Error("Failed to load stations");
        }

        allStations =
            await response.json();

        updateDashboard();

        renderStations(allStations);

    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <div class="loading-card">
                <h3>Unable to load stations</h3>
                <p>
                    Make sure the Java server and MySQL
                    are running.
                </p>
            </div>
        `;
    }
}


// ==========================================
// DASHBOARD STATISTICS
// ==========================================

function updateDashboard() {

    const total =
        allStations.length;

    const available =
        allStations.filter(
            station =>
                station.status.toLowerCase() ===
                "available"
        ).length;

    const power =
        allStations.reduce(
            (sum, station) =>
                sum +
                Number(station.power_kw || 0),
            0
        );

    const locations =
        new Set(
            allStations.map(
                station =>
                    station.location
                        .toLowerCase()
                        .trim()
            )
        ).size;


    document.getElementById(
        "totalStations"
    ).textContent = total;


    document.getElementById(
        "availableStations"
    ).textContent = available;


    document.getElementById(
        "totalPower"
    ).textContent =
        `${power.toFixed(1)} kW`;


    document.getElementById(
        "locationsCount"
    ).textContent = locations;
}


// ==========================================
// RENDER STATIONS
// ==========================================

function renderStations(stations) {

    const container =
        document.getElementById(
            "stationContainer"
        );

    container.innerHTML = "";


    if (stations.length === 0) {

        container.innerHTML = `
            <div class="loading-card">

                <h3>No stations found</h3>

                <p>
                    Try a different search or add
                    a new charging station.
                </p>

            </div>
        `;

        return;
    }


    stations.forEach(station => {

        const card =
            document.createElement("div");

        card.className =
            "station-card";


        const statusClass =
            getStatusClass(station.status);


        card.innerHTML = `

            <div class="station-card-top">

                <div>

                    <h3>
                        ${escapeHtml(
                            station.station_name
                        )}
                    </h3>

                    <div class="station-location">
                        📍
                        ${escapeHtml(
                            station.location
                        )}
                    </div>

                </div>


                <div
                    class="status ${statusClass}">

                    <span class="status-dot"></span>

                    ${escapeHtml(
                        station.status
                    )}

                </div>

            </div>


            <div class="station-info">

                <div class="info-item">

                    <div class="info-label">
                        Slot
                    </div>

                    <div class="info-value">
                        ${escapeHtml(
                            station.charging_slot
                        )}
                    </div>

                </div>


                <div class="info-item">

                    <div class="info-label">
                        Connector
                    </div>

                    <div class="info-value">
                        ${escapeHtml(
                            station.connector_type
                        )}
                    </div>

                </div>


                <div class="info-item">

                    <div class="info-label">
                        Power
                    </div>

                    <div class="info-value">
                        ${station.power_kw} kW
                    </div>

                </div>


                <div class="info-item">

                    <div class="info-label">
                        Station ID
                    </div>

                    <div class="info-value">
                        #${station.id}
                    </div>

                </div>

            </div>


            <div class="station-bottom">

                <div class="price">

                    ₹${station.price_per_unit}

                    <span>
                        / unit
                    </span>

                </div>


                <button
                    class="details-button"
                    onclick="showStationDetails(${station.id})">

                    View Details

                </button>

            </div>

        `;


        container.appendChild(card);

    });
}


// ==========================================
// STATUS CLASS
// ==========================================

function getStatusClass(status) {

    const value =
        String(status)
            .toLowerCase();

    if (value === "available") {
        return "available";
    }

    if (value === "occupied") {
        return "occupied";
    }

    return "maintenance";
}


// ==========================================
// SEARCH
// ==========================================

document
    .getElementById("searchInput")
    .addEventListener(
        "input",
        filterStations
    );


document
    .getElementById("statusFilter")
    .addEventListener(
        "change",
        filterStations
    );


function filterStations() {

    const search =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase()
            .trim();


    const status =
        document
            .getElementById("statusFilter")
            .value
            .toLowerCase();


    const filtered =
        allStations.filter(station => {

            const matchesSearch =
                station.station_name
                    .toLowerCase()
                    .includes(search)

                ||

                station.location
                    .toLowerCase()
                    .includes(search);


            const matchesStatus =
                status === "all"

                ||

                station.status
                    .toLowerCase()
                    === status;


            return (
                matchesSearch &&
                matchesStatus
            );

        });


    renderStations(filtered);
}


// ==========================================
// SHOW DETAILS
// ==========================================

function showStationDetails(id) {

    const station =
        allStations.find(
            item =>
                Number(item.id) === Number(id)
        );


    if (!station) {
        return;
    }


    const modal =
        document.getElementById(
            "stationModal"
        );


    const content =
        document.getElementById(
            "modalContent"
        );


    content.innerHTML = `

        <span class="section-tag">
            STATION DETAILS
        </span>


        <h2>
            ${escapeHtml(
                station.station_name
            )}
        </h2>


        <p>
            📍
            ${escapeHtml(
                station.location
            )}
        </p>


        <div class="modal-details">

            <div class="modal-detail">

                <span>
                    Charging Slot
                </span>

                <strong>
                    ${escapeHtml(
                        station.charging_slot
                    )}
                </strong>

            </div>


            <div class="modal-detail">

                <span>
                    Connector
                </span>

                <strong>
                    ${escapeHtml(
                        station.connector_type
                    )}
                </strong>

            </div>


            <div class="modal-detail">

                <span>
                    Status
                </span>

                <strong>
                    ${escapeHtml(
                        station.status
                    )}
                </strong>

            </div>


            <div class="modal-detail">

                <span>
                    Power
                </span>

                <strong>
                    ${station.power_kw} kW
                </strong>

            </div>


            <div class="modal-detail">

                <span>
                    Price
                </span>

                <strong>
                    ₹${station.price_per_unit}
                    / unit
                </strong>

            </div>


            <div class="modal-detail">

                <span>
                    Station ID
                </span>

                <strong>
                    #${station.id}
                </strong>

            </div>

        </div>

    `;


    modal.classList.remove("hidden");
}


// ==========================================
// CLOSE MODAL
// ==========================================

function closeModal() {

    document
        .getElementById("stationModal")
        .classList.add("hidden");
}


document
    .getElementById("stationModal")
    .addEventListener(
        "click",
        function(event) {

            if (
                event.target === this
            ) {

                closeModal();

            }

        }
    );


// ==========================================
// ADD STATION
// ==========================================

document
    .getElementById("stationForm")
    .addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const message =
                document.getElementById(
                    "message"
                );


            const formData =
                new FormData(this);


            const data =
                new URLSearchParams(
                    formData
                );


            message.textContent =
                "Saving station...";


            try {

                const response =
                    await fetch(
                        "/api/stations",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded"
                            },

                            body: data
                        }
                    );


                const result =
                    await response.json();


                if (!response.ok) {

                    throw new Error(
                        result.error ||
                        "Unable to save station"
                    );

                }


                message.textContent =
                    "✓ Station added successfully.";


                this.reset();


                await loadStations();


                setTimeout(() => {

                    message.textContent = "";

                }, 4000);


            } catch (error) {

                console.error(error);


                message.textContent =
                    "✕ " + error.message;

            }

        }
    );


// ==========================================
// SECURITY
// ==========================================

function escapeHtml(value) {

    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ==========================================
// START
// ==========================================

loadStations();