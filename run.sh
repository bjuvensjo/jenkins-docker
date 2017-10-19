docker run -d -i -t -p 8080:8080 -p 50000:50000 --rm --name jenkins --env-file .env --env JAVA_OPTS=-Djenkins.install.runSetupWizard=false -v $(PWD)/jenkins_home:/var/jenkins_home bjuvensjo/jenkins
