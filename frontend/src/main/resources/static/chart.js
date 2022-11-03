loadScript("https://cdn.jsdelivr.net/npm/chart.js", () => {
    const graphData = {
        labels: labels,
        datasets: [{
            label: 'Total',
            backgroundColor: 'rgb(255, 255, 255)',
            borderColor: 'rgb(204,0,0)',
            data: data
        }]
    };

    const config = {
        type: 'line',
        data: graphData,
        options: {
            plugins: {
                legend: {
                    display: false
                },
                tooltips: {
                    enabled: false
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    ticks: {
                        display: false
                    }
                },
                y: {
                    min: 0
                }
            }
        }
    };

    const char = new Chart(document.getElementById('chart'), config);
});