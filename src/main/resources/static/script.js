document.addEventListener('DOMContentLoaded', function () {
    fetchCourses();
});

function fetchCourses() {
    fetch('/api/courses')  
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#courseTable tbody');
            data.forEach(course => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${course.courseName}</td>
                    <td>${course.description}</td>
                    <td>${course.instructorName}</td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching courses:', error));
}
