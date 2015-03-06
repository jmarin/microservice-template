version := "1.0.0"

enablePlugins(JavaServerAppPackaging)

maintainer in Linux := "Juan Marin Otero <juan.marin.otero@gmail.com>"

packageSummary in Linux := "Scala Microservice template"

packageDescription := "Microservice template with Akka HTTP and Docker"

daemonUser in Linux := normalizedName.value         // user which will execute the application

daemonGroup in Linux := (daemonUser in Linux).value // group which will execute the application

maintainer in Docker := "Juan Marin Otero <juan.marin.otero@gmail.com>"

//daemonUser in Docker := normalizedName.value // user in the Docker image which will execute the application (must already exist)


// WARNING: DON'T DO THIS IN PRODUCTION DOING THIS TO GET IT WORKING (base Docker image doesn't have a lot of users to choose from. )
daemonUser in Docker := "root"

dockerBaseImage := "dockerfile/java" // Docker image to use as a base for the application image

dockerExposedPorts in Docker := Seq(8080) // Ports to expose from container for Docker container linking

