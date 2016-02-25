package com.peterphi.std.guice.common.introspective;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peterphi.std.guice.common.introspective.type.DatabaseIntrospectiveInfo;

@Singleton
public class IntrospectiveInfoRegistry {
    @Inject(optional = true)
    DatabaseIntrospectiveInfo databaseIntrospectiveInfo;

    public DatabaseIntrospectiveInfo getDatabaseIntrospectiveInfo()
    {
        return databaseIntrospectiveInfo;
    }
}
