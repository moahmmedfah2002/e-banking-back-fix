# Use official Tomcat 10 image with JDK 17 (Jakarta EE 10 requires Java 11+)
FROM tomcat:10.1-jdk17-temurin

# Set environment variables (optional)
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true"
ENV DEPLOY_DIR /usr/local/tomcat/webapps

# Remove default Tomcat apps (optional)
RUN rm -rf $DEPLOY_DIR/*

# Copy your WAR file to Tomcat's webapps directory
COPY target/banque-1.0-SNAPSHOT.war $DEPLOY_DIR/ROOT.war

# Alternative: if you want to keep the original WAR name
# COPY target/your-application.war $DEPLOY_DIR/

# Expose port 8080 (Tomcat default)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]