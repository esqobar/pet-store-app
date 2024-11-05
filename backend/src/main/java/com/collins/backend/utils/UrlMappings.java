package com.collins.backend.utils;

public class UrlMappings {
    /*============================ Start Api Users ===================================*/
    public static final String API = "/api/v1";
    public static final String USERS_API = API+"/users";
    public static final String NEW_USER = "/register";
    public static final String USER_UPDATE = "/update/{userId}";
    public static final String USER_DELETE = "/delete/{userId}";
    public static final String USER_BY_ID = "/user/{userId}";
    public static final String ALL_USERS = "/all";

    /*============================ Start Api Appointments ===================================*/
    public static final String APPOINTMENTS_API = API+"/appointments";
    public static final String ALL_APPOINTMENTS = "/all";
    public static final String NEW_APPOINTMENT = "/book-appointment";
    public static final String APPOINTMENT_UPDATE = "/update/{id}";
    public static final String APPOINTMENT_DELETE = "/delete/{id}";
    public static final String APPOINTMENT_BY_ID = "/appointment/{id}";
    public static final String APPOINTMENT_BY_NO = "/appointmentNo/{appointmentNo}";

    /*============================ Start Pet API ===================================*/
    public static final String PETS_API =API+"/pets" ;
    public static final String SAVE_PETS_FOR_APPOINTMENT ="/save-pets" ;
    public static final String PET_BY_ID = "/pet/{petId}" ;
    public static final String DELETE_PET = "/delete/{petId}" ;
    public static final String UPDATE_PET = "/update/{petId}" ;

    /*============================ Start Pet API ===================================*/
    public static final String PHOTOS_API =API+"/photos" ;
    public static final String PHOTO_UPLOAD ="/upload" ;
    public static final String PHOTO_BY_ID = "/photo/{imageId}" ;
    public static final String DELETE_PHOTO = "/delete/{imageId}/user/{userId}" ;
    public static final String UPDATE_PHOTO = "/update/{imageId}" ;
}
