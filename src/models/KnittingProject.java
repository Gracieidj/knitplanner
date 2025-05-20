package models;

/**
 * Represents a knitting or crochet project with various attributes
 * that can be used for filtering and display.
 */
public class KnittingProject {
    // Name of the project (e.g., "Cozy Winter Scarf")
    private String name;

    // Skill level required to complete the project (e.g., "Beginner", "Intermediate", "Advanced")
    private String skillLevel;

    // Type of yarn recommended or used for the project (e.g., "Wool", "Cotton", "Acrylic")
    private String yarnType;

    // Aesthetic or style category of the project (e.g., "Classic", "Boho", "Modern")
    private String aesthetic;

    // Type of craft: either "Knitting" or "Crochet"
    private String projectType;

    // Weather suitability for the project (e.g., "Cold", "Warm", "All")
    private String suitableWeather;

    /**
     * Constructor to initialize all fields of the project.
     *
     * @param name            Project name
     * @param skillLevel      Skill level required
     * @param yarnType        Yarn type used
     * @param aesthetic       Style or aesthetic category
     * @param projectType     Craft type: "Knitting" or "Crochet"
     * @param suitableWeather Suitable weather condition(s) for the project
     */
    public KnittingProject(String name, String skillLevel, String yarnType, String aesthetic, String projectType, String suitableWeather) {
        this.name = name;
        this.skillLevel = skillLevel;
        this.yarnType = yarnType;
        this.aesthetic = aesthetic;
        this.projectType = projectType;
        this.suitableWeather = suitableWeather;
    }

    // Getters for all fields (setters can be added if mutability is needed)

    /** Returns the project name */
    public String getName() { return name; }

    /** Returns the skill level required */
    public String getSkillLevel() { return skillLevel; }

    /** Returns the yarn type used */
    public String getYarnType() { return yarnType; }

    /** Returns the aesthetic/style category */
    public String getAesthetic() { return aesthetic; }

    /** Returns the project type ("Knitting" or "Crochet") */
    public String getProjectType() { return projectType; }

    /** Returns the suitable weather category */
    public String getSuitableWeather() { return suitableWeather; }

    /**
     * Returns a string representation of the project,
     * combining the name and the project type.
     */
    @Override
    public String toString() {
        return name + " (" + projectType + ")";
    }
}
