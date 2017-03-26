package com.example.santa.anative.network.connection;

import com.example.santa.anative.model.entity.Package;

/**
 * Created by santa on 26.03.17.
 */

public class PackageBuilder {

    private static PackageBuilder mPackageBuilder;

    public static PackageBuilder getDefault() {
        if (mPackageBuilder == null) mPackageBuilder = new PackageBuilder();
        return mPackageBuilder;
    }

    public String build(Package pack) {

        return "";
    }

}
