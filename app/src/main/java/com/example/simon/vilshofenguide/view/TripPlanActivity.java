package com.example.simon.vilshofenguide.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.sightseeing.Category;
import com.example.simon.vilshofenguide.sightseeing.Path;
import com.example.simon.vilshofenguide.com.example.simon.vilshofenguide.controller.PathChangeController;
import com.example.simon.vilshofenguide.sightseeing.Sight;
import com.example.simon.vilshofenguide.sightseeing.SightManager;
import com.example.simon.vilshofenguide.sightseeing.TripConfigurations;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 29.12.2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TripPlanActivity extends AppCompatActivity implements PathShower, PopupMenu.OnMenuItemClickListener{

    private static final String[] possibleTimes = new String[]{"0:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30"};
    private SightManager manager;
    private PathChangeController pathChanger;
    private int choosenSightIdInList;
    private ListView choosenPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plan);

        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");

        choosenPathView = (ListView)findViewById(R.id.route_list);
        registerForContextMenu(this.choosenPathView);

        findViewById(R.id.view_route_on_map).setEnabled(false);

        initSpinners();

        initTimeFields();

        this.pathChanger = (PathChangeController)getIntent().getSerializableExtra("path");
        this.pathChanger.registerPathShower(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initTimeFields(){
        Spinner t = ((Spinner) findViewById(R.id.trip_time));
        SpinnerAdapter timeSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, possibleTimes);
        t.setAdapter(timeSpinnerAdapter);
    }

    /**
     * Initializes the spinners with the possible answers
     */
    private void initSpinners() {
        Spinner arrivalSpinner = (Spinner)findViewById(R.id.arrival_place);
        SpinnerAdapter arrivalSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new LinkedList<>(manager.getAllSights()));
        arrivalSpinner.setAdapter(arrivalSpinnerAdapter);

        Spinner departureSpinner = (Spinner)findViewById(R.id.departure_place);
        SpinnerAdapter departureSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new LinkedList<>(manager.getAllSights()));
        departureSpinner.setAdapter(departureSpinnerAdapter);

        Spinner focusSpinner = (Spinner)findViewById(R.id.focus);
        SpinnerAdapter focusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Category.values());
        focusSpinner.setSelection(3);
        focusSpinner.setAdapter(focusSpinnerAdapter);
    }

    /**
     * Sets the shown route to the given one
     * @param p
     */
    private void showPath(Path p){
        if (p == null){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.exception_no_such_route), Toast.LENGTH_LONG).show();
            return;
        }
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, p.asList());
        this.choosenPathView.setAdapter(listAdapter);
        TextView calculatedDepartureTime = (TextView)findViewById(R.id.calculated_departure_time);
    }

    /**
     * Collects the information about the route in the form and informs the pathShower to calculate the best route
     */
    public void calculateRoute(View view){
        int tripTime;
        try{
            String endString = ((Spinner)findViewById(R.id.trip_time)).getSelectedItem().toString();
            tripTime = Integer.parseInt(endString.split(":")[0]) * 60 + Integer.parseInt(endString.split(":")[1]);
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.exception_trip_time_format), Toast.LENGTH_LONG).show();
            return;
        }

        Category c = (Category)((Spinner) findViewById(R.id.focus)).getSelectedItem();

        TripConfigurations tc = TripConfigurations.getDefaultTripConfigurations(c, tripTime, manager.getAnySight(), manager.getAnySight());

        tc.setStartSight((Sight)((Spinner)findViewById(R.id.arrival_place)).getSelectedItem());
        tc.setEndSight((Sight)((Spinner)findViewById(R.id.departure_place)).getSelectedItem());

        try{
            this.pathChanger.setTripCofigurations(tc);
            this.pathChanger.calculateRoute(manager);
            findViewById(R.id.view_route_on_map).setEnabled(true);
        }catch(Exception e){
            changeToErrorActivity(e);
        }
    }

    /**
     * Shows the route on the map
     */
    public void viewOnMap(View view){
        changeToMapActivity();
    }

    private void changeToMapActivity(){
        Intent intent = new Intent(this, VilshofenMapActivity.class);
        intent.putExtra("path", this.pathChanger);
        intent.putExtra("sightManager", this.manager);
        startActivity(intent);
    }

    private void changeToSightDetailActivity(Sight s){
        Intent intent = new Intent(this, SightDetailActivity.class);
        intent.putExtra("path", this.pathChanger);
        intent.putExtra("sightManager", this.manager);
        intent.putExtra("sight", s);
        startActivity(intent);
    }

    private void changeToErrorActivity(Exception e){
        Intent intent = new Intent(this, ErrorActivity.class);
        intent.putExtra("exception", e);
        startActivity(intent);
    }

    @Override
    public void setPath(Path p) {
        showPath(p);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.configure_sight_list_cancel_context_menu:
                return true;
            default:
                for (Sight s : this.manager.getAllSights()){
                    if (s.getId() == item.getItemId()){
                        this.pathChanger.itemAdded(s, manager);
                        return true;
                    }
                }
                return false;
        }
    }

    /**
     * Creates a context menu that allows the user to configure the calculates route by adding/removing sights
     * and offers the possibility to look at the further description
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //Saves the information which list entry in the route list has been choosen when the change menu has been opened
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        this.choosenSightIdInList = ((Sight)this.choosenPathView.getItemAtPosition(acmi.position)).getId();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_route_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configure_sight_list_show_sight_description:
                this.changeToSightDetailActivity(manager.getSightById(choosenSightIdInList));
                return true;
            case R.id.configure_sight_list_delete_item:
                try {
                    this.pathChanger.itemDeleted(manager.getSightById(this.choosenSightIdInList), manager);
                } catch (Exception e) {
                    changeToErrorActivity(e);
                    return false;
                }
                return true;
            case R.id.configure_sight_list_add_sight:
                this.openAddRoutePopupMenu();
                return true;
            case R.id.configure_sight_list_cancel_context_menu:
                return true;
            default:
                return false;
        }
    }

    /**
     * Opens the menu that allows the user to choose a sight which will be added to the route
     */
    private void openAddRoutePopupMenu() {
        PopupMenu menu = new PopupMenu(this, findViewById(R.id.route_list));
        for (Sight s : this.manager.getAllSights()){
            menu.getMenu().add(Menu.NONE, s.getId(), Menu.NONE, s.toString());
        }
        menu.getMenu().add(Menu.NONE, R.id.configure_sight_list_cancel_context_menu, Menu.NONE, getApplicationContext().getString(R.string.cancel));
        menu.setOnMenuItemClickListener(this);
        menu.show();
    }
}