package knitplanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.KnittingProject;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of KnittingProject objects, including
 * loading from and saving to a JSON file,
 * adding new projects, and suggesting projects based on filters.
 */
public class ProjectSuggester {
    // List holding all knitting projects in memory
    private final List<KnittingProject> allProjects;

    // Filename where projects are persisted in JSON format
    private static final String SAVE_FILE = "projects.json";

    /**
     * Constructor initializes the projects list and loads existing projects from disk.
     * If loading fails or no projects exist, default projects are added and saved.
     */
    public ProjectSuggester() {
        allProjects = new ArrayList<>();
        loadProjects();
    }

    /**
     * Loads projects from the JSON file specified by SAVE_FILE.
     * If the file doesn't exist or is empty, populates the list with default projects and saves them.
     */
    private void loadProjects() {
        try (Reader reader = new FileReader(SAVE_FILE)) {
            // Define the type for Gson deserialization (List of KnittingProject)
            Type listType = new TypeToken<List<KnittingProject>>() {}.getType();

            // Deserialize JSON content into a list of projects
            List<KnittingProject> loadedProjects = new Gson().fromJson(reader, listType);

            if (loadedProjects != null && !loadedProjects.isEmpty()) {
                // Successfully loaded projects - add them to allProjects
                allProjects.addAll(loadedProjects);
            } else {
                // No projects found in file - add defaults and save
                addDefaultProjects();
                saveProjects();
            }
        } catch (IOException e) {
            // Could not read file (not found or other IO issue),
            // so add default projects and save them
            addDefaultProjects();
            saveProjects();
        }
    }

    /**
     * Saves the current list of projects to the JSON file.
     */
    private void saveProjects() {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            // Serialize the list of projects to JSON and write to file
            new Gson().toJson(allProjects, writer);
        } catch (IOException e) {
            // Print stack trace if saving fails
            e.printStackTrace();
        }
    }

    /**
     * Adds a set of default knitting projects to the projects list.
     * These provide sample data if no saved projects exist.
     */
    private void addDefaultProjects() {
        allProjects.add(new KnittingProject("Cozy Winter Scarf", "Beginner", "Wool", "Classic", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Light Cotton Shawl", "Intermediate", "Cotton", "Boho", "Knitting", "Warm"));
        allProjects.add(new KnittingProject("Modern Acrylic Blanket", "Advanced", "Acrylic", "Modern", "Knitting", "All"));
        allProjects.add(new KnittingProject("Classic Wool Hat", "Beginner", "Wool", "Classic", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Boho Cotton Bag", "Intermediate", "Cotton", "Boho", "Crochet", "Warm"));
        allProjects.add(new KnittingProject("Acrylic Baby Sweater", "Beginner", "Acrylic", "Classic", "Knitting", "All"));
        allProjects.add(new KnittingProject("Modern Wool Gloves", "Advanced", "Wool", "Modern", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Classic Cotton Socks", "Intermediate", "Cotton", "Classic", "Knitting", "All"));
        allProjects.add(new KnittingProject("Boho Acrylic Vest", "Advanced", "Acrylic", "Boho", "Knitting", "Warm"));
        allProjects.add(new KnittingProject("Wool Cable Knit Sweater", "Advanced", "Wool", "Classic", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Cotton Summer Top", "Beginner", "Cotton", "Modern", "Knitting", "Warm"));
        allProjects.add(new KnittingProject("Acrylic Throw Pillow", "Intermediate", "Acrylic", "Boho", "Crochet", "All"));
        allProjects.add(new KnittingProject("Classic Wool Mittens", "Beginner", "Wool", "Classic", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Modern Cotton Dishcloths", "Beginner", "Cotton", "Modern", "Crochet", "Warm"));
        allProjects.add(new KnittingProject("Boho Wool Headband", "Intermediate", "Wool", "Boho", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Acrylic Cozy Socks", "Intermediate", "Acrylic", "Classic", "Knitting", "All"));
        allProjects.add(new KnittingProject("Cotton Market Tote", "Advanced", "Cotton", "Boho", "Crochet", "Warm"));
        allProjects.add(new KnittingProject("Wool Ear Warmers", "Beginner", "Wool", "Modern", "Knitting", "Cold"));
        allProjects.add(new KnittingProject("Classic Acrylic Blanket", "Intermediate", "Acrylic", "Classic", "Knitting", "All"));
        allProjects.add(new KnittingProject("Boho Crochet Scarf", "Advanced", "Cotton", "Boho", "Crochet", "Warm"));
        // Add more default projects here if desired
    }

    /**
     * Adds a new project to the collection and persists the change.
     * @param project the new KnittingProject to add
     */
    public void addProject(KnittingProject project) {
        allProjects.add(project);
        saveProjects();
    }

    /**
     * Suggests projects filtered by the given criteria.
     * Any filter that is null or empty is ignored.
     * 
     * @param skill filter by skill level (e.g., "Beginner")
     * @param yarn filter by yarn type (e.g., "Wool")
     * @param aesthetic filter by aesthetic style (e.g., "Modern")
     * @param weather filter by suitable weather (e.g., "Cold")
     * @return list of projects matching all specified filters
     */
    public List<KnittingProject> suggestProjects(String skill, String yarn, String aesthetic, String weather) {
        List<KnittingProject> results = new ArrayList<>();
        
        // Debug output for filter parameters
        System.out.println("Filtering with: skill=" + skill + ", yarn=" + yarn + ", aesthetic=" + aesthetic + ", weather=" + weather);

        for (KnittingProject p : allProjects) {
            // Check each filter - ignore filter if null or blank

            boolean matchesSkill = (skill == null || skill.isBlank()) || p.getSkillLevel().equalsIgnoreCase(skill);
            boolean matchesYarn = (yarn == null || yarn.isBlank()) || p.getYarnType().equalsIgnoreCase(yarn);
            boolean matchesAesthetic = (aesthetic == null || aesthetic.isBlank()) || p.getAesthetic().equalsIgnoreCase(aesthetic);

            // Weather matching allows "All" to match any weather filter, or vice versa
            String projectWeather = p.getSuitableWeather();
            boolean matchesWeather = (weather == null || weather.isBlank()) ||
                    projectWeather.equalsIgnoreCase("All") || weather.equalsIgnoreCase("All") ||
                    projectWeather.equalsIgnoreCase(weather);

            // If all filters match, add project to results
            if (matchesSkill && matchesYarn && matchesAesthetic && matchesWeather) {
                results.add(p);
            } else {
                // Debug output for projects filtered out
                System.out.println("Filtered out: " + p.getName() + " (skill=" + p.getSkillLevel()
                        + ", yarn=" + p.getYarnType()
                        + ", aesthetic=" + p.getAesthetic()
                        + ", weather=" + p.getSuitableWeather() + ")");
            }
        }

        // Debug output for number of matches found
        System.out.println("Total matches: " + results.size());

        return results;
    }
}
