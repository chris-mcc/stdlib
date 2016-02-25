package com.peterphi.std.guice.hibernate.introspective;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterphi.std.guice.common.introspective.type.DatabaseIntrospectiveInfo;
import com.peterphi.std.guice.database.annotation.Transactional;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
@Singleton
public class HibernateIntrospectiveInfoRetriever implements Work
{
    Logger log = Logger.getLogger(HibernateIntrospectiveInfoRetriever.class);
    private String dataBaseUrl;
    private String dataBaseProductName;
    private String driverName;
    private String dbUsername;
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public void execute(Connection connection) throws SQLException
    {
        try {
            dataBaseUrl = connection.getMetaData().getURL();
            dataBaseProductName = connection.getMetaData().getDatabaseProductName();
            driverName = connection.getMetaData().getDriverName();
            dbUsername = connection.getMetaData().getUserName();
        }
        catch(Exception e)
        {
            log.warn("Failed to get DB connection info",e);
        }
    }

    @Transactional(readOnly = true)
    public DatabaseIntrospectiveInfo getIntrospectiveInfo()
    {
        Session session = sessionFactory.getCurrentSession();
        session.doWork(this);
        DatabaseIntrospectiveInfo info = new DatabaseIntrospectiveInfo();
        info.dataBaseProductName = this.dataBaseProductName;
        info.dataBaseUrl = this.dataBaseUrl;
        info.driverName = this.driverName;
        info.dbUsername = this.dbUsername;
        return info;
    }
}
