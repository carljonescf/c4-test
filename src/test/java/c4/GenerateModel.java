package c4;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.WorkspaceApiClient;
import com.structurizr.component.ComponentFinder;
import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.ComponentFinderStrategyBuilder;
import com.structurizr.component.TypeRepository;
import com.structurizr.component.filter.IncludeFullyQualifiedNameRegexFilter;
import com.structurizr.component.matcher.AnnotationTypeMatcher;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.component.supporting.*;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;

public class GenerateModel {


    private final static int WORKSPACE_ID = 87359;
    private final static String API_KEY = "7f683dd6-ced2-4a52-9ca4-6b352d2a504f";
    private final static String API_SECRET = "c26dad05-63af-4dff-900f-fab3ef73041b";


    @Test //comment out to run manually
    public void generateModel() throws Exception {

        //set up workspace and model
        //these are the core objects of our
        Workspace workspace = new Workspace("Testing C4 with Spring", "Sample Spring Boot Project for 2023");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem lotsOfSpam = model.addSoftwareSystem("Lots of Spam", "Online takeaway of Spam, Spam, Spam and more Spam");
        Person owner = model.addPerson("Takeaway Owner", "  A person who plans the menu.");



        owner.uses(lotsOfSpam, "Uses");



        Container webApplication = lotsOfSpam.addContainer(
                "Spring Boot Application", "The web application", "Embedded web container.  Tomcat 7.0");
        Container relationalDatabase = lotsOfSpam.addContainer(
                "Relational Database", "Stores information regarding the products.", "MySQL");
        owner.uses(webApplication, "Uses", "HTTP");


        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC, port 3306");

        new ComponentFinderBuilder()
                .forContainer(webApplication)
                .fromClasses("build/libs/C4-checker-0.0.1-SNAPSHOT.jar")
                .fromSource("src/main/java")

                //find repositories by annotation.
               .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new AnnotationTypeMatcher("org.springframework.stereotype.Repository"))
                                .build()
                )
                //find repositories by suffix.
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Repository"))
                                .supportedBy(new ImplementationWithSuffixSupportingTypesStrategy  ("Impl"))
                                .forEach(component -> component.uses(relationalDatabase, "Reads from and writes to"))
                                .withTechnology("Spring Data Repository")
                                .build()
                )
                //find services by annotation.
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new AnnotationTypeMatcher("org.springframework.stereotype.Service"))
                                .build()
                )
                //find services by suffix
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new NameSuffixTypeMatcher("Service"))
                                .supportedBy(new ImplementationWithSuffixSupportingTypesStrategy  ("Impl"))
                                .build()
                )

                //find controllers by annotation
                .withStrategy(
                        new ComponentFinderStrategyBuilder()
                                .matchedBy(new AnnotationTypeMatcher("org.springframework.stereotype.Controller"))
                                .withTechnology("Spring MVC Controller")
                                .supportedBy(new AllReferencedTypesInPackageSupportingTypesStrategy())
                                .forEach((component -> {
                                    owner.uses(component, "uses");
                                    component.addTags(component.getTechnology());
                                }))

                                .build()
                )
                .build().run();

        System.out.println("Components");
        //print components
        webApplication.getComponents().forEach(c -> {
            System.out.println(c.getName());
        });

        System.out.println("Relationships");

        //print relationships
        webApplication.getComponents().forEach(component -> {
            component.getRelationships().forEach(System.out::println);
        });


        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(lotsOfSpam, "context", "The System Context diagram for the Lots of Spam system.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.enableAutomaticLayout();

        ContainerView containerView = viewSet.createContainerView(lotsOfSpam, "containers", "The Containers diagram for the Lots of Spam system.");
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();
        containerView.enableAutomaticLayout();

        ComponentView componentView = viewSet.createComponentView(webApplication, "components", "The Components diagram for the Lots of Spam web application.");

        webApplication.getComponents().forEach(c -> componentView.add(c, true));

        componentView.addAllPeople();
        componentView.add(relationalDatabase);
        componentView.enableAutomaticLayout();


        // link the architecture model with the code
/*        for (Component component : webApplication.getComponents()) {
            for (CodeElement codeElement : component.getCode()) {
                String sourcePath = codeElement.getUrl();
                if (sourcePath != null) {
                    codeElement.setUrl(
                            "https://git.cardiff.ac.uk/teaching/commercial-applications-with-java/2022-2023/sample-projects/charity-giving-2022/-/tree/main");
                }
            }
        }*/

        // rather than creating a component model for the database, let's simply link to the DDL
        // (this is really just an example of linking an arbitrary element in the model to an external resource)
        //relationalDatabase.setUrl("https://github.com/spring-projects/spring-petclinic/tree/master/src/main/resources/db/hsqldb");

        // tag and style some elements

        lotsOfSpam.addTags("Lots of Spam 2023");
        relationalDatabase.addTags("Database");


        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle("Lots of Spam 2023").background("#6CB33E").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#519823").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#91D366").color("#ffffff");
        styles.addElementStyle("Database").shape(Shape.Cylinder);

/*        styles.addElementStyle("Spring REST Controller").background("#D4FFC0").color("#000000");

        styles.addElementStyle("Spring MVC Controller").background("#D4F3C0").color("#000000");
        styles.addElementStyle("Spring Service").background("#6CB33E").color("#000000");
        styles.addElementStyle("Spring Repository").background("#95D46C").color("#000000");*/


        // add ADRs

/*        File adrDirectory = new File("./src/main/adr");
        AdrToolsDecisionImporter decisionImporter = new AdrToolsDecisionImporter();
        decisionImporter.importDocumentation(lotsOfSpam, adrDirectory);*/


        //System.setProperty("jdk.tls.client.protocols","TLSv1,TLSv1.1,TLSv1.2");

        WorkspaceApiClient client = new WorkspaceApiClient(API_KEY, API_SECRET);
        client.putWorkspace(WORKSPACE_ID, workspace);

    }


}

