import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.plugins.credentials.impl.*;
import hudson.model.*
import hudson.security.*
import hudson.tasks.Maven
import jenkins.model.*
import org.jfrog.*
import org.jfrog.hudson.*

def instance = Jenkins.getInstance()

// JDK
instance.setJDKs([new JDK("jdk8", System.env.get("JAVA_HOME"))])

// Maven
def maven = instance.getExtensionList(Maven.DescriptorImpl.class)[0]
maven.installations = [new Maven.MavenInstallation("MAVEN", "/opt/maven", [])]
maven.save()

// Security
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin","admin")
instance.setSecurityRealm(hudsonRealm)
instance.save()
 
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()

// Credentials
Credentials stash = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "e814603b-72e9-4ae6-9eee-30292a7441d6", "stash", System.getenv().get("STASH_USER"), System.getenv().get("STASH_PASSWORD"))
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), stash)
SystemCredentialsProvider.getInstance().save()

// Executors
instance.setNumExecutors(4)

// Views  
instance.setViews([new AllView("All"), new ListView("Synchronized")])

// Save
instance.save()
