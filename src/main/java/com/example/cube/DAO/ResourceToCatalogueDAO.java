package com.example.cube.DAO;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceToCatalogueDAO extends DBConnect {

    public void createReference(Resource resource, Catalogue catalogue) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }

    public List<Catalogue> getCatalogueByResource(Resource resource) throws SQLException{
        List<Catalogue> catalogues = new ArrayList<>();
        Catalogue emptyCatalogue = new Catalogue();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getCatalogueByResource(?)");
        preparedStatement.setInt(1, resource.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            emptyCatalogue.setId(resultSet.getInt(1));
            emptyCatalogue.setCategory(resultSet.getString(2));
            catalogues.add(emptyCatalogue);
        }

        return catalogues;
    }

    public List<Resource> getReferenceByCatalogue(Catalogue catalogue) throws SQLException{
        List<Resource> resources = new ArrayList<>();
        Resource emptyResource = new Resource();

        PreparedStatement preparedStatement = conn().prepareStatement("CALL getResourceByCatalogue(?)");
        preparedStatement.setInt(1, catalogue.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            emptyResource.setId(resultSet.getInt(1));
            emptyResource.setAccess(resultSet.getString(2));
            emptyResource.setValue(resultSet.getString(3));
            emptyResource.setJoint(resultSet.getString(4));
            resources.add(emptyResource);
        }

        return resources;
    }

    public void deleteReference(Resource resource, Catalogue catalogue) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }
}
