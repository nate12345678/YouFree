
# You Free?
HackIllinois2020 project

Built an android and web app to help users plan to meet with friends, using a PostgreSQL server.

Users can fill theirs schedules through the application, and other users can see when they are next available to meet.

We host our own website and database to give us more flexibility with our implementation and significantly faster deployments.

The Android app was built with both Java and Kotlin, and implemented the REST Api using OKHttp3 and object conversion using Gson. It uses a custom layout to generate calendar views based on server data.

The web app part of the project was created using React JS and programmed in JavaScript, using some CSS for nicer looking interfaces. We used material_ui framework to make the user interface on the app a lot more user friendly.

The server is implemented in Java with the Spring Boot framework to configure the endpoints and interface with the PostreSQL database.
