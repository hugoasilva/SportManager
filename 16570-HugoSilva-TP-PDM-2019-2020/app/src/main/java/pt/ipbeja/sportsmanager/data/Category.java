package pt.ipbeja.sportsmanager.data;

import java.util.Objects;

/**
 * Category Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-05
 */
public class Category {
    private final int id;
    private String name;

    /**
     * Category object constructor
     *
     * @param id   category's id
     * @param name category's name
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get category id
     *
     * @return category id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get category name
     *
     * @return category name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get category as String object
     *
     * @return category as String
     */
    @Override
    public String toString() {
        return "Category{" +
                "id=" + this.id +
                ", name=" + this.name +
                '}';
    }

    /**
     * Get category hash code
     *
     * @return category hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
}
