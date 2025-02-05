const getGraphMetricMaxSalesValue = (metricBars) => {
    let maxSalesValue = 0;

    metricBars.forEach(metricBar => {
        const salesValue = parseFloat(metricBar.getAttribute('data-value'));
        let newMaxSalesValue = 0;

        if (salesValue >= 0 && salesValue <= 5000) {
            newMaxSalesValue = 5000;
        } else if (salesValue > 5000 && salesValue <= 10000) {
            newMaxSalesValue = 10000;
        } else if (salesValue > 10000 && salesValue <= 25000) {
            newMaxSalesValue = 25000;
        } else if (salesValue > 25000 && salesValue <= 50000) {
            newMaxSalesValue = 50000;
        } else if (salesValue > 50000 && salesValue <= 100000) {
            newMaxSalesValue = 100000;
        } else if (salesValue > 100000 && salesValue <= 250000) {
            newMaxSalesValue = 250000;
        } else if (salesValue > 250000 && salesValue <= 500000) {
            newMaxSalesValue = 500000;
        } else if (salesValue > 500000 && salesValue <= 1000000) {
            newMaxSalesValue = 1000000;
        } else if (salesValue > 1000000 && salesValue <= 5000000) {
            newMaxSalesValue = 5000000;
        } else if (salesValue > 5000000 && salesValue <= 10000000) {
            newMaxSalesValue = 10000000;
        } else if (salesValue > 10000000 && salesValue <= 50000000) {
            newMaxSalesValue = 50000000;
        } else if (salesValue > 50000000 && salesValue <= 100000000) {
            newMaxSalesValue = 50000000;
        } else {
        }

        maxSalesValue = Math.max(maxSalesValue, newMaxSalesValue);

        let newHeight = 0 + '%';

        if (salesValue > 1000000000) {
            newHeight = 100 + '%';
        } else {
            newHeight = ((salesValue / maxSalesValue) * 100) + '%';
        }

        metricBar.style.height = newHeight;
    });

    return maxSalesValue;
}

const setMetricBarValues = () => {
    const metricBars = document.querySelectorAll('.metric-bar');
    const maxSalesValue = getGraphMetricMaxSalesValue(metricBars);

    //Set new height of bars according to max sales value
    metricBars.forEach(metricBar => {
        const salesValue = metricBar.getAttribute('data-value');
        let newHeight;

        if (salesValue > 1000000000) {
            newHeight = 100 + '%';
        } else {
            newHeight = ((salesValue / maxSalesValue) * 100) + '%';
        }

        metricBar.style.height = newHeight;
    });

    updateMetricYAxis(maxSalesValue);
}

const updateMetricYAxis = (maxSalesValue) => {
    const yAxisItems = document.querySelectorAll('.metric-bar-y-axis-item');
    let incrementValue = 1;

    yAxisItems.forEach(item => {
        let newSalesValue = Math.floor(maxSalesValue * incrementValue);
        incrementValue -= 0.2;

        item.innerHTML = newSalesValue.toLocaleString('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 0 });
    });
}

document.addEventListener('DOMContentLoaded', setMetricBarValues);