package com.em7.wol.service;

import com.em7.wol.dto.out.OutDeviceDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Service
public class PingService {

    private final Integer shutdownPort = 7801;

    public void pingDevicesConcurrently(List<OutDeviceDTO> outDeviceDTOs) {
        // Create a thread pool with a fixed number of threads (can be customized)
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            // Loop through each device and submit a callable task to the executor
            for (OutDeviceDTO device : outDeviceDTOs) {
                Callable<Void> task = () -> {
                    try {
                        InetAddress address = InetAddress.getByName(device.getIp());
                        boolean reachable = address.isReachable(500); // 100ms timeout should be fine, normally we are on LAN but some devices like ESPs may take longer to respond
                        device.setShutdownable(checkShutdownService(device.getIp()));
                        device.setStatus(reachable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                };

                // Submit the task for concurrent execution
                executorService.submit(task);
            }
        } finally {
            // Shutdown the executor service after all tasks are submitted
            executorService.shutdown();
            try {
                // Wait for all tasks to complete or timeout after 1 minute
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    //System.err.println("Some tasks did not finish in time!");
                } else {
                    //System.out.println("All tasks completed successfully.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkShutdownService(String ip){
        ConcurrentLinkedQueue openPorts = new ConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Integer> portsToScan = new ArrayList<>(Arrays.asList(shutdownPort));
        for(Integer port : portsToScan){
            executorService.submit(() -> {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), 500);
                    socket.close();
                    openPorts.add(port);
                } catch (IOException e) {}
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List openPortList = new ArrayList<>();
        while (!openPorts.isEmpty()) {
            openPortList.add(openPorts.poll());
        }
        return openPortList.contains(shutdownPort);
    }
}
