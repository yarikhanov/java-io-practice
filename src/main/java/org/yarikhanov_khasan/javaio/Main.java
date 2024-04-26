package org.yarikhanov_khasan.javaio;

import org.yarikhanov_khasan.javaio.view.LabelView;
import org.yarikhanov_khasan.javaio.view.PostView;
import org.yarikhanov_khasan.javaio.view.WriterView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WriterView writerView = new WriterView();
        PostView postView = new PostView();
        LabelView labelView = new LabelView();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Выберите сущность для работы:");
            System.out.println("1 - Писатели (Writers)");
            System.out.println("2 - Посты (Posts)");
            System.out.println("3 - Метки (Labels)");
            System.out.println("0 - Выход");

            System.out.print("Введите число (0-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    writerView.run();
                    break;
                case 2:
                    postView.run();
                    break;
                case 3:
                    labelView.run();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте снова.");
                    break;
            }
        }

        scanner.close();
    }
}