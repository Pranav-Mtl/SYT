package com.syt.constant;

import java.util.ArrayList;

/**
 * Created by pranav on 4/8/2015.
 * purpose: declare all static variables used throughout the app
 */
public class Constant  {

   public static final String GOOGLE_PROJ_ID = "871793772386";//378376053947
   public static final String MSG_KEY = "m";    // Webservice url


    public static String WEBSERVICE_URL="https://sellyourtime.in/api/";
    public static String WEBSERVICE_Register="step_one_register.php?";
    public static String WEBSERVICE_SocialLogin="social_login.php?";
    public static String WEBSERVICE_Login="login.php?";
    public static String WEBSERVICE_Profile="profile.php?";
    public static String WEBSERVICE_BuyerRegister="buyer_register.php?";
    public static String WEBSERVICE_SellerRegister="seller_register.php?";
    public static String WEBSERVICE_ForgotPassword="forgot.php?";
    public static String WEBSERVICE_ForgotPasswordCode="forgot_code.php?";
    public static String WEBSERVICE_CategoriesList="categories_list.php?";
    public static String WEBSERVICE_SubCategoriesList="rest_of_categories.php";
    public static String WEBSERVICE_SellerFragment="ticker.php?";
    public static String WEBSERVICE_BuyerFragment="ticker_buyer.php?";
    public static String WEBSERVICE_SellerProfile="fetch_profile.php?";
    public static String WEBSERVICE_ChangePassword="change_password.php?";
    public static String WEBSERVICE_BuyerProfile="buyer_profile.php?";
    public static String WEBSERVICE_Category="categories_count.php?";
    public static String WEBSERVICE_CategorySearch="search.php?";
    public static String WEBSERVICE_Filter="filter.php?";
    public static String WEBSERVICE_SubTrainerTutorList= "category_sub_sub_trainer_tutor.php?";
    public static String WEBSERVICE_SubITMarketing= "category_sub_sub_it_marketing.php?";
    public static String WEBSERVICE_UserProfile= "logged_in.php?";
    public static String WEBSERVICE_EditProfile= "edit_basic_profile.php?";
    public static String WEBSERVICE_UpdateProfile="edit_profile.php?";
    public static String WEBSERVICE_ManageMainCategory="manage_main_category.php?";
    public static String WEBSERVICE_ManageCategory="manage_category.php?";
    public static String WEBSERVICE_Location="location.php?";
    public static String WEBSERVICE_MyPosts="fetch_post_requirement.php?";
    public static String WEBSERVICE_SearchPost="search_post.php?";
    public static String WEBSERVICE_SellerRating= "rateus.php?";
    public static String WEBSERVICE_OTP= "otp.php?";
    public static int CategoryImageWidth;
    public static int CategoryImageHeight;
    public static String WEBSERVICE_DeleteCategory= "delete_category.php?";



    //seller leads Array

    public static String sellerNameArray[];
    public static String sellerCategoryArray[];
    public static String sellerZipArray[];
    public static String sellerSubCategoryArray[];
    public static String sellerIdArray[];

    public static String profileId="profile";
    public static String appURL="url";

    ////buyer leads Array

    public static String buyerNameArray[];
    public static String buyerCategoryArray[];
    public static String buyerZipArray[];
    public static String buyerIdArray[];
    public static String buyerSubCategoryArray[];

    public static String SearchNameArray[];
    public static String SearchCategoryArray[];
    public static String SearchZipArray[];
    public static String SearchIdArray[];
    public static String SearchSubCategoryArray[];

    public static String categoryFirstNameArray[];
    public static String categoryLastNameArray[];
    public static String categoryCategoryArray[];
    public static String categoryZipArray[];
    public static String categoryIdArray[];
    public static String categorySubCategoryArray[];
    public static String categoryPriceMinArray[];
    public static String categoryPriceMaxArray[];
    public static String categoryAvailabilityArray[];
    public static String categoryExperienceArray[];
    public static String categoryImageArray[];

    public static String followIdArray[];
   public static String followFirstNameArray[];
   public static String followLastNameArray[];
 public static String followCategoryArray[];
 public static String followSubCategoryArray[];
 public static String followImageArray[];
 public static String followLocationArray[];

    public static String myPostCategory[];
    public static String myPostSubCategory[];
    public static String myPostId[];
    public static String myPostDate[];
    public static String myPostTitle[];

    public static ArrayList list;


    public static  String PREFS_NAME = "MyPrefsFile";

    public static String SHARED_PREFERENCE_UserID="USERID";
    public static String SHARED_PREFERENCE_SearchTitle="SEARCH";
    public static String SHARED_PREFERENCE_RegistrationID="REGISTRATION";
    public static String SHARED_PREFERENCE_LoginTitle="LOGIN";
    public static String CampaignLadoo="ladooo";

    //public static String REGISTER_SELECTION=null;
    public static String ERR_INTERNET_CONNECTION_NOT_FOUND_MSG="Click YES to open settings or Click NO to go back";

    public  static String LoginMedium;

    public  static String RegisterTitle;

    public static String userOTP;

    public static String SocialMediaLoginTitle;

    public static String ERR_INTERNET_CONNECTION_NOT_FOUND="No Internet Connection Found";


    public static  String STREMAILADDREGEX_FORGOTPASSWORD="^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$";

    // Linked in
    public static String CONSUMER_KEY="754liwy4ijkhzz";
    public static String CONSUMER_SECRET="3rCLqrgZQXdxFCp5";
    public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
    public static final String OAUTH_CALLBACK_HOST = "litestcalback";
    public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}
