package com.aman.code.model;

import java.util.List;

public class Person {
    private String id;
    private String fullName = "";
    private String address = "";
    private Integer age;
    private List<String> cities;
    private List<String> schools;
    private List<String> colleges;
    private String currentOrganization = "";
    private List<String> pastOrganizations;
    private List<String> interests;

    public Person(String id, String fullName, String address, Integer age,
                  List<String> cities, List<String> schools, List<String> colleges,
                  String currentOrganization, List<String> pastOrganizations, List<String> interests) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.age = age;
        this.cities = cities;
        this.schools = schools;
        this.colleges = colleges;
        this.currentOrganization = currentOrganization;
        this.pastOrganizations = pastOrganizations;
        this.interests = interests;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public List<String> getColleges() {
        return colleges;
    }

    public void setColleges(List<String> colleges) {
        this.colleges = colleges;
    }

    public String getCurrentOrganization() {
        return currentOrganization;
    }

    public void setCurrentOrganization(String currentOrganization) {
        this.currentOrganization = currentOrganization;
    }

    public List<String> getPastOrganizations() {
        return pastOrganizations;
    }

    public void setPastOrganizations(List<String> pastOrganizations) {
        this.pastOrganizations = pastOrganizations;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
