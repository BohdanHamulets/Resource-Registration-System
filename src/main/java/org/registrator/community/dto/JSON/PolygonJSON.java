package org.registrator.community.dto.JSON;

import org.registrator.community.entity.Area;

import java.util.List;

/**
 * Created by Oleksiy on 02.01.2016.
 */
public class PolygonJSON {
    private String resourceDescription;
    private String identifier;
    private String resourceType;
    private List<PointJSON> points;

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<PointJSON> getPoints() {
        return points;
    }

    public void setPoints(List<PointJSON> points) {
        this.points = points;
    }
}