document.addEventListener('DOMContentLoaded', () => {
    if (dateFromDb) {
        document.getElementById('date').value = dateFromDb;
    }
    if (earliestCheckInFromDb) {
        document.getElementById('earliestCheckIn').value = earliestCheckInFromDb;
    }
});