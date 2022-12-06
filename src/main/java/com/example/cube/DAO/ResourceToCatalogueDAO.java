package com.example.cube.DAO;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Service.DBConnect;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResourceToCatalogueDAO extends DBConnect {

    public void createReference(Resource resource, Catalogue catalogue) throws SQLException {
        PreparedStatement preparedStatement = conn().prepareStatement("CALL createReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }

    public void getReferenceByResource(Resource resource) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL getReferenceByResource(?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.execute();
    }

    public void getReferenceByCatalogue(Catalogue catalogue) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL getReferenceByCatalogue(?)");
        preparedStatement.setInt(1, catalogue.getId());
        preparedStatement.execute();
    }

    public void deleteReference(Resource resource, Catalogue catalogue) throws SQLException{
        PreparedStatement preparedStatement = conn().prepareStatement("CALL deleteReference(?,?)");
        preparedStatement.setInt(1, resource.getId());
        preparedStatement.setInt(2, catalogue.getId());
        preparedStatement.execute();
    }
}
