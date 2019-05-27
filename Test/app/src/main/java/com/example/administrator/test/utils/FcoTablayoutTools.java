package com.example.administrator.test.utils;

import com.example.administrator.test.entity.QQTopGroup;
import com.example.administrator.test.event.GeDan;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.utils
 */
public class FcoTablayoutTools {

    public static ArrayList<CustomTabEntity> getEntities(String[] strings) {
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for(int i = 0;i<strings.length;i++){
            int finalI = i;
            CustomTabEntity entity = new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return strings[finalI];
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            };
            entities.add(entity);
        }
        return entities;
    }

    public static ArrayList<CustomTabEntity> getEntities(List<GeDan> geDans) {
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for(int i = 0;i<geDans.size();i++){
            int finalI = i;
            CustomTabEntity entity = new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return geDans.get(finalI).getItem_name();
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            };
            entities.add(entity);
        }
        return entities;
    }

    public static ArrayList<CustomTabEntity> getEntities2(List<QQTopGroup> qqTopGroups) {
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for(int i = 0;i<qqTopGroups.size();i++){
            int finalI = i;
            CustomTabEntity entity = new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return qqTopGroups.get(finalI).getGroupName();
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            };
            entities.add(entity);
        }
        return entities;
    }
}
