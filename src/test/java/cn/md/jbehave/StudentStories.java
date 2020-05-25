package cn.md.jbehave;

import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import static org.jbehave.core.io.CodeLocations.*;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;

public class StudentStories extends JUnitStories {  
  
    public Configuration configuration() {  
        return new MostUsefulConfiguration().useStoryLoader(new LoadFromClasspath(this.getClass()))  
                .useStoryReporterBuilder(new StoryReporterBuilder().withCodeLocation(codeLocationFromClass(this.getClass())));  
    }  
  
    @Override  
    protected List<String> storyPaths() {  
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()),"**/*.story","");  
    }  
} 