package com.example.my_automation.constants;

public final class Constants {

    public static final class Architecture {
        public static final String ARCH_PROJECT_NAME = "arch-name";
        public static final String AUTOMATION_LAYOUT_NAME = "automation layout";

    }

    public static final class Interior {
        public static final String STATUS_FINISHED = "STATUS_FINISHED";
        public static final String DATE_CREATED = "date_created";
        public static final String CREATED_AT = "createdAt";
        public static final String PROJECTS_ALL = "all";
        public static final String PROJECTS_RESULTS = "results";
        public static final String USER_ID = "userId";


        public static final class ScheduleRepetition {
            public static final String MINUTELY = "MINUTELY";
            public static final String HOURLY = "HOURLY";
            public static final String DAILY = "DAILY";
            public static final String WEEKLY = "WEEKLY";
            public static final String MONTHLY = "MONTHLY";
        }

        public static final class ScheduleStatus {
            public static final String ACTIVE = "ACTIVE";
            public static final String INACTIVE = "INACTIVE";
            public static final String COMPLETED = "COMPLETED";
        }

        public static final class RecommendationActionResult {
            public static final String SUCCESS = "success";

        }

    }

    public static final class Headers {
        public static final String ACCEPT = "Accept";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CACHE_CONTROL = "Cache-control";
        public static final String AUTHORIZATION_TOKEN = "AuthorizationToken";
        public static final String AUTHORIZATION = "Authorization";
        public static final String APPLICATION_JSON = "application/json";
        public static final String NO_CACHE = "no-cache";
        public static final String BEARER = "Bearer ";
    }

    public static final class MediaTypes {
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_PDF = "application/pdf";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        public static final String CONTENT_TYPE_JPEG = "image/jpeg";
        public static final String CONTENT_TYPE_MP3 = "audio/mp3";
        public static final String CONTENT_TYPE_MP4 = "video/mp4";
    }


    public static final class Parameters {
        public static final String PAGE_PARAM = "page";
        public static final String PAGE_SIZE = "pageSize";
        public static final String SORT = "sort";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String PROJECT_ID = "projectId";
        public static final String PROJECT_NAME = "project-name";
        public static final String TEMP_PROJECTS = "temp_projects";
        public static final String RELATIONS = "relations";
        public static final String CLOSED = "closed";
        //archive
        public static final String ARCHIVE = "archive";
        public static final String REGULAR = "Regular";
        public static final String MEDIUM = "Medium";
        public static final String HIGH = "High";
    }

    public static final class StatusCodes {
        public static final int SUCCESS_CODE = 200;
        public static final int CREATED_CODE = 201;
        public static final int FORBIDDEN_CODE = 403;
        public static final int NOT_FOUND_CODE = 404;
        public static final int CONFLICT_CODE = 409;
    }

    public static final class Permissions {
        public static final String READ = "Read";
        public static final String READWRITE = "ReadWrite";
        public static final String EVERYTHING = "Everything";
    }

    public static final class ElasticFields {
        public static final String OBJECT_KEY = "object_key";
    }

    public static final String PACKAGE = "com.example";

    public static final int DEFAULT_CONNECTION_TIMEOUT = 120 * 1000;


    public static final String SHORT_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";

    public static final String MEMBERS = "members";
    public static final String TIMEZONE_GMT = "GMT";

    public static final class Sorting {
        public static final String CREATION_DATE_ASC = "creationDate asc";
        public static final String NAME_DESC = "name desc";
        public static final String NAME_ASC = "name asc";
        public static final String MODIFIED_DESC = "modified desc";
        public static final String MODIFIED_ASC = "modified asc";
        public static final String PRIORITY_DESC = "priority desc";
        public static final String PRIORITY_ASC = "priority asc";
        public static final String DUE_DATE_DESC = "dueDate desc";
        public static final String SCHEDULED_DESC = "scheduled desc";
        public static final String SCHEDULED_ASC = "scheduled asc";
        public static final String DEFAULT_SORTING = "creationDate desc";
        public static final String ANALYTICS = "analytics"
    }
}


