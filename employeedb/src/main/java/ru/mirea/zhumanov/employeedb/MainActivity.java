package ru.mirea.zhumanov.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


import ru.mirea.zhumanov.employeedb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDataBase appDataBase = App.getInstance().getDatabase();
        EmployeeDAO employeeDAO = appDataBase.employeeDAO();

        Employee employee = new Employee();
        employee.id = 1;
        employee.name = "Star-Lord";
        employee.salary = 1000;
        employeeDAO.insert(employee);

        List<Employee> employees = employeeDAO.getAll();
        employee = employeeDAO.getByID(1);
        employee.salary = 2000;
        employeeDAO.update(employee);
        Log.d(getClass().getSimpleName(), employee.name + " " + employee.salary);
    }
}