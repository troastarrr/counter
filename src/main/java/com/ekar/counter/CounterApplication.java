package com.ekar.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ekar.counter"})
@EnableJpaRepositories("com.ekar.counter.repository")
public class CounterApplication {

    public static void main(String[] args) {

        SpringApplication.run(CounterApplication.class, args);

//
//		String rts = "Antique,Batangas,Batangas,Benguet,Biliran,Bohol,Bulacan,Bulacan,Bulacan,Bulacan,Capiz,Capiz,
//		Capiz,Cavite,Cavite,Cavite,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Cebu,Davao de Oro,Davao Del
//		Norte,Davao Del Norte,Davao Del Norte,Davao Del Sur,Davao Del Sur,Davao Del Sur,Davao Del Sur,Davao Del Sur,
//		Ilocos Norte,Ilocos Norte,Iloilo,Iloilo,Isabela,Isabela,Isabela,Isabela,Isabela,Isabela,La Union,Laguna,
//		Laguna,Laguna,Laguna,Laguna,Laguna,Lanao Del Norte,Leyte,Leyte,Leyte,Metro Manila~Caloocan,Metro
//		Manila~Caloocan,Metro Manila~Las Pinas,Metro Manila~Las Pinas,Metro Manila~Makati,Metro Manila~Mandaluyong,
//		Metro Manila~Mandaluyong,Metro Manila~Mandaluyong,Metro Manila~Manila,Metro Manila~Manila,Metro Manila~Manila,
//		Metro Manila~Muntinlupa,Metro Manila~Muntinlupa,Metro Manila~Navotas,Metro Manila~Paranaque,Metro
//		Manila~Paranaque,Metro Manila~Pasay,Metro Manila~Pasig,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro
//		Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro
//		Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Quezon City,Metro Manila~Valenzuela,Misamis Oriental,
//		Misamis Oriental,Misamis Oriental,Negros Occidental,Negros Oriental,North Cotabato,North Cotabato,Nueva Ecija,
//		Nueva Ecija,Nueva Ecija,Nueva Ecija,Pampanga,Pampanga,Pampanga,Pampanga,Pampanga,Pampanga,Quezon,Rizal,Rizal,
//		Sorsogon,Sorsogon,South Cotabato,South Cotabato,South Cotabato,Sultan Kudarat,Tarlac,Zamboanga Del Norte,
//		Zamboanga Del Sur,Zamboanga Sibugay,Zamboanga Sibugay";
//
//		List<String> convertedCountriesList = Arrays.asList(StringUtils.splitPreserveAllTokens(rts, ","));
//
//		Map<String,Long> count = new HashMap<>();
//		for (String location : convertedCountriesList) {
//			if (count.containsKey(location)) {
//				long number = count.get(location);
//				count.put(location,number+1);
//			} else {
//				count.put(location,1L);
//			}
//		}
//		for (Map.Entry<String,Long> cc : count.entrySet()) {
//			System.out.println("Location : " + cc.getKey() + " Count :" + cc.getValue() );
//		}

    }
}
