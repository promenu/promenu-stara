package sk.ideacorp.promenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;

public class RestaurantActivity extends AppCompatActivity {

    private String url = "";

    private String restaurant_name = "";

    private String restaurant_name_2 = "";

    private String slug = "";

    private String mobile_id = "";

    private String latitude = "";

    private String longitude = "";

    private String restaurant_latitude = "";

    private String restaurant_longitude = "";

    private Browser browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Bundle bundle = getIntent().getExtras();
        this.restaurant_name = bundle.getString("restaurant_name");
        this.slug = bundle.getString("slug");
        this.mobile_id = bundle.getString("mobile_id");
        this.latitude = bundle.getString("latitude");
        this.longitude = bundle.getString("longitude");

        TextView page_name = (TextView)findViewById(R.id.page_name);

        restaurant_name_2 = (restaurant_name.length() > 30) ? restaurant_name.substring(0, 30) + "..." : restaurant_name;

        page_name.setText(restaurant_name_2);

        Toolbar about_toolbar = (Toolbar)findViewById(R.id.toolbar);
        about_toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    RestaurantActivity.this.onBackPressed();
                }

                return true;
            }
        });

        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    RestaurantActivity.this.onBackPressed();

                    return true;
                }

                return true;
            }
        });

        this.url = "http://mobile.promenu.sk/api/restauracia/" + this.slug;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                try {
                    Restaurants restaurants = new Restaurants(RestaurantActivity.this);

                    String html = "";
                    html += restaurants.getHeader2();

                    String str = new String(response, "UTF-8");

                    JSONObject jsonObject = new JSONObject(str);
                    String o_nas = jsonObject.optString("o_nas").toString();
                    String is_favorite = jsonObject.optString("is_favorite").toString();
                    String actual_day = jsonObject.optString("actual_day").toString();
                    String openned = jsonObject.optString("openned").toString();
                    String address = jsonObject.optString("address").toString();
                    String city_name = jsonObject.optString("city_name").toString();
                    String zip_code = jsonObject.optString("zip_code").toString();
                    String rozvoz = jsonObject.optString("rozvoz").toString();
                    String phone = jsonObject.optString("kontakt_tel").toString();
                    String email = jsonObject.optString("kontakt_email").toString();
                    String web = jsonObject.optString("kontakt_web").toString();

                    restaurant_latitude = jsonObject.optString("lat").toString();
                    restaurant_longitude = jsonObject.optString("lon").toString();

                    JSONArray menu_array = jsonObject.getJSONArray("dens");

                    JSONArray jedalnicek_array = jsonObject.getJSONArray("jedalnicek");

                    JSONArray images_array = jsonObject.getJSONArray("images");

                    int i_is_favorite = (is_favorite.length() > 0) ? Integer.parseInt(is_favorite) : 0;

                    html += "<div class=\"restaurant_page\"><div class=\"day_selector\">";
                    html += "<a href=\"#\" data-href=\"#monday\" class=\"first " + ((actual_day.indexOf("1") >= 0) ? "active" : "") + "\">Po</a>";
                    html += "<a href=\"#\" data-href=\"#tuesday\" class=\"" + ((actual_day.indexOf("2") >= 0) ? "active" : "") + "\">Ut</a>";
                    html += "<a href=\"#\" data-href=\"#wednesday\" class=\"" + ((actual_day.indexOf("3") >= 0) ? "active" : "") + "\">St</a>";
                    html += "<a href=\"#\" data-href=\"#thursday\" class=\"" + ((actual_day.indexOf("4") >= 0) ? "active" : "") + "\">Št</a>";
                    html += "<a href=\"#\" data-href=\"#friday\" class=\"" + ((actual_day.indexOf("5") >= 0) ? "active" : "") + "\">Pi</a>";
                    html += "<a href=\"#\" data-href=\"#saturday\" class=\"" + ((actual_day.indexOf("6") >= 0) ? "active" : "") + "\">So</a>";
                    html += "<a href=\"#\" data-href=\"#sunday\" class=\"last " + ((actual_day.indexOf("7") >= 0) ? "active" : "") + "\">Ne</a>";
                    html += "</div>";

                    if(menu_array.length() > 0) {
                        for(int i = 0; i < menu_array.length(); i++) {
                            int day = i + 1;

                            JSONObject menuJsonObject = menu_array.getJSONObject(i);

                            JSONArray menu_item_array = menuJsonObject.getJSONArray("polozkas");

                            html += "<div class=\"menu_day_box\" id=\"" + RestaurantActivity.this.get_day_name(day) + "\" " + ((actual_day.indexOf(String.valueOf(day)) < 0) ? "style=\"display: none;\"" : "") + ">";

                            html += "<table style=\"margin-top: -1px;\" class=\"table_" + day + "\"><tbody>";

                            for(int j = 0; j < menu_item_array.length(); j++) {
                                JSONObject menuItemJsonObject = menu_item_array.getJSONObject(j);

                                String menu_item_name = menuItemJsonObject.optString("nazov").toString();

                                String menu_item_price = menuItemJsonObject.optString("cena").toString();

                                html += "<tr class=\"" + (((j % 2) == 0) ? "even" : "odd") + "\">";

                                if(menu_item_price.length() > 0) {
                                    html += "<td>" + menu_item_name + "</td>";
                                    html += "<td style=\"white-space: nowrap;\">" + menu_item_price + "</td>";
                                } else {
                                    html += "<td colspan=\"2\">" + menu_item_name + "</td>";
                                }
                            }

                            html += "</tbody></table></div>";
                        }
                    }

                    if(jedalnicek_array.length() > 0) {
                        html += "<a href=\"javascript:void(0);\" id=\"food_list\" data-name=\"" + restaurant_name + "\" data-slug=\"" + slug + "\" data-latitude=\"" + restaurant_latitude + "\" data-longitude=\"" + restaurant_longitude + "\" data-mobile_id=\"" + mobile_id + "\" class=\"food_list_link\">Jedálny lístok</a>";
                    }

                    if(openned.length() > 0) {
                        html += "<h2><strong>Dnes otvorené: </strong>" + openned + "</h2>";
                    }

                    html += "<div class=\"favourite\">";

                    if(i_is_favorite == 1) {
                        html += "<a href=\"javascript:void(0);\" id=\"add_to_favorite\" style=\"display: none;\">Pridať do obľúbených</a>";
                        html += "<a href=\"javascript:void(0);\" id=\"remove_from_favorite\">Odobrať z obľúbených</a>";
                    } else {
                        html += "<a href=\"javascript:void(0);\" id=\"add_to_favorite\">Pridať do obľúbených</a>";
                        html += "<a href=\"javascript:void(0);\" id=\"remove_from_favorite\" style=\"display: none;\">Odobrať z obľúbených</a>";
                    }

                    html += "</div>";

                    html += "<div class=\"info_box\"> <div class=\"section group\"> <div class=\"col span_1_of_2\"><h3>" + restaurant_name_2 + "</h3>";

                    html += "<p class=\"address\"> <i class=\"fa fa-home\"></i>";

                    if(address.length() > 0) {
                        html += address + "<br />";
                    }

                    if(city_name.length() > 0) {
                        html += city_name + "<br />";
                    }

                    if(zip_code.length() > 0) {
                        html += zip_code;
                    }

                    html += "</p>";
                    html += "<p class=\"phone\"><i class=\"fa fa-phone\"></i>";
                            /*{if strlen(@$restaurant->phone) > 1}
                    <a href="tel:{@$restaurant->phone}">{@$restaurant->phone}</a>
                            {else}
                    Nie je k dispozícii
                    {/if}*/

                    if(phone != null && phone != "null" && phone.length() > 0) {
                        String phone_short = (phone.length() > 20) ? phone.substring(0, 20) : phone;

                        html += " <a href=\"tel:" + phone + "\">" + phone_short + "</a>";
                    } else {
                        html += "Nie je k dispozícii";
                    }

                    html += "</p>";

                    html += "<p class=\"delivery\"><i class=\"fa fa-automobile\"></i>";

                    if(rozvoz != null && rozvoz != "null" && rozvoz.length() > 0) {
                        String rozvoz_short = (rozvoz.length() > 20) ? rozvoz.substring(0, 20) : rozvoz;

                        html += " <a href=\"tel:" + rozvoz + "\">" + rozvoz_short + "</a>";
                    } else {
                        html += "Nie je k dispozícii";
                    }

                    html += "</p>";

                    html += "<p class=\"web\"><i class=\"fa fa-globe\"></i>";

                    if(web != null && web != "null" && web.length() > 0) {
                        String web_short = (web.length() > 20) ? web.substring(0, 20) : web;

                        int http_position = web.indexOf("http://");

                        if(http_position >= 0) {
                            html += " <a href=\"web:" + web + "\" target=\"_blank\">" + web_short.replace("http://", "") + "</a>";
                        } else {
                            html += " <a href=\"http://web:" + web + "\" target=\"_blank\">" + web_short + "</a>";
                        }
                    } else {
                        html += "Nie je k dispozícii";
                    }

                    html += "</p>";

                    html += "<p class=\"email\"><i class=\"fa fa-envelope\"></i>";

                    if(email != null && email != "null" && email.length() > 0) {
                        String email_short = (email.length() > 20) ? email.substring(0, 20) : email;

                        html += " <a href=\"mailto:" + email + "\">" + email_short + "</a>";
                    } else {
                        html += "Nie je k dispozícii";
                    }

                    html += "</p>";

                    html += "</div><div class=\"col span_1_of_2\">";

                    html += "<a href=\"javascript:void(0);\" id=\"restaurant_map\" data-name=\"" + restaurant_name + "\" data-slug=\"" + slug + "\" data-latitude=\"" + restaurant_latitude + "\" data-longitude=\"" + restaurant_longitude + "\" data-mobile_id=\"" + mobile_id + "\"><img src=\"http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=16&size=200x200&maptype=roadmap&sensor=false&language=&markers=color:red|label:none|" + latitude + "," + longitude + "\" width=\"200\" height=\"200\" /></a>";

                    html +="</div></div></div>";

                    if(images_array.length() > 0) {
                        html += "<div id=\"jssor_1\" style=\"position: relative; margin: 0 auto; top: 0px; left: 0px; width: 100%; height: 100%; overflow: hidden; visibility: hidden;\">" +
                                "<div data-u=\"slides\" style=\"cursor: default; position: relative; top: 0px; left: 0px; width: 100%; height: 100%; overflow: hidden;\">";
                        for(int i=0; i < images_array.length(); i++) {
                            JSONObject avatarJsonObject = images_array.getJSONObject(i);

                            String avatar_url = avatarJsonObject.optString("avatar_url").toString();

                            html += "<div style=\"display: none;\">" +
                                    "<a href=\"http://www.promenu.sk" + avatar_url + "\" rel=\"gallery\">" +
                                    "<img src=\"http://www.promenu.sk" + avatar_url + "\" alt=\"\" title=\"\" />" +
                                    "</a>" +
                                    "</div>";
                        }

                        html += "</div></div>";
                    }

                    html += "<div class=\"desc\">" + o_nas + "</div>";

                    html += "</div>";

                    html += restaurants.getFooter();

                    Browser browser = new Browser((WebView)RestaurantActivity.this.findViewById(R.id.webViewMain), RestaurantActivity.this, (ProgressBar)RestaurantActivity.this.findViewById(R.id.mainProgressBar));
                    browser.setHTMLWithAssets(html);

                } catch (Exception e) {
                    e.printStackTrace();

                    MessageBox messageBox = new MessageBox(RestaurantActivity.this, "Chyba", "Chyba pri načítaní dát");
                    messageBox.Show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                MessageBox messageBox = new MessageBox(RestaurantActivity.this, "Chyba", "Chyba pri spojení so serverom");
                messageBox.Show();
            }

            @Override
            public void onRetry(int retryNo) {}
        });
    }

    public String get_day_name(int day) {

        String day_name ="";

        switch(day) {
            case 1: day_name = "monday"; break;
            case 2: day_name = "tuesday"; break;
            case 3: day_name = "wednesday"; break;
            case 4: day_name = "thursday"; break;
            case 5: day_name = "friday"; break;
            case 6: day_name = "saturday"; break;
            case 7: day_name = "sunday"; break;
        }

        return day_name;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
