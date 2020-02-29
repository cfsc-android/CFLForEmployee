package com.chanfinecloud.cflforemployee.entity;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 是否解决枚举
 */
public enum SolveType {
    Solve(0,"已解决"),
    UnSolve(1,"未解决");

    private final int type;
    private final String name;
    SolveType(int type,String name){
        this.type=type;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public static String getNameByType(int type) {
        for(SolveType solveType:SolveType.values()){
            if(solveType.getType()==type){
                return solveType.getName();
            }
        }
        return "";
    }
}
