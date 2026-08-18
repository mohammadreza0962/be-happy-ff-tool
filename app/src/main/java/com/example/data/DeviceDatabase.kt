package com.example.data

import com.example.model.DeviceProfile
import com.example.model.PerformanceProfile

object DeviceDatabase {

    val allDevices: List<DeviceProfile> = listOf(
        // Samsung
        DeviceProfile("Samsung", "Galaxy A15", "6GB", PerformanceProfile.BALANCED, 180, 384),
        DeviceProfile("Samsung", "Galaxy A14", "4GB", PerformanceProfile.BALANCED, 180, 384),
        DeviceProfile("Samsung", "Galaxy A25 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Samsung", "Galaxy A35 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Samsung", "Galaxy A54 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Samsung", "Galaxy A55 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Samsung", "Galaxy S21 FE", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Samsung", "Galaxy S23 Ultra", "12GB", PerformanceProfile.ULTRA, 240, 440, true),
        DeviceProfile("Samsung", "Galaxy S24 Ultra", "12GB", PerformanceProfile.ULTRA, 240, 440, true),
        DeviceProfile("Samsung", "Galaxy A05", "4GB", PerformanceProfile.LOW, 120, 360),
        DeviceProfile("Samsung", "Galaxy A04e", "3GB", PerformanceProfile.LOW, 120, 360),

        // Xiaomi / POCO / Redmi
        DeviceProfile("POCO", "X6 Pro 5G", "8GB", PerformanceProfile.ULTRA, 480, 420, true),
        DeviceProfile("POCO", "X6 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("POCO", "M6 Pro", "8GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("POCO", "F5 Pro", "12GB", PerformanceProfile.ULTRA, 480, 440, true),
        DeviceProfile("POCO", "F6 Pro", "12GB", PerformanceProfile.ULTRA, 480, 440, true),
        DeviceProfile("POCO", "C65", "4GB", PerformanceProfile.LOW, 180, 360),
        DeviceProfile("Redmi", "Note 13 Pro+ 5G", "12GB", PerformanceProfile.ULTRA, 240, 420, true),
        DeviceProfile("Redmi", "Note 13 4G", "6GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Redmi", "Note 12", "4GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Redmi", "13C", "4GB", PerformanceProfile.LOW, 180, 360),
        DeviceProfile("Redmi", "12 4G", "4GB", PerformanceProfile.LOW, 180, 360),
        DeviceProfile("Redmi", "9A", "2GB", PerformanceProfile.LOW, 120, 360),
        DeviceProfile("Xiaomi", "13T Pro", "12GB", PerformanceProfile.ULTRA, 480, 440, true),
        DeviceProfile("Xiaomi", "14 Ultra", "12GB", PerformanceProfile.ULTRA, 480, 450, true),

        // Realme
        DeviceProfile("Realme", "GT 6T", "8GB", PerformanceProfile.ULTRA, 360, 420, true),
        DeviceProfile("Realme", "12 Pro+ 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Realme", "11 5G", "8GB", PerformanceProfile.HIGH, 240, 392),
        DeviceProfile("Realme", "C67", "6GB", PerformanceProfile.BALANCED, 180, 384),
        DeviceProfile("Realme", "C55", "6GB", PerformanceProfile.BALANCED, 180, 384),
        DeviceProfile("Realme", "C53", "6GB", PerformanceProfile.BALANCED, 180, 384),
        DeviceProfile("Realme", "C33", "3GB", PerformanceProfile.LOW, 120, 360),
        DeviceProfile("Realme", "Narzo 60x", "6GB", PerformanceProfile.BALANCED, 180, 392),

        // Infinix
        DeviceProfile("Infinix", "GT 20 Pro", "12GB", PerformanceProfile.ULTRA, 360, 420, true),
        DeviceProfile("Infinix", "GT 10 Pro", "8GB", PerformanceProfile.ULTRA, 360, 411, true),
        DeviceProfile("Infinix", "Note 40 Pro", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Infinix", "Note 30 VIP", "12GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Infinix", "Hot 40 Pro", "8GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Infinix", "Hot 30", "8GB", PerformanceProfile.BALANCED, 180, 392),
        DeviceProfile("Infinix", "Smart 8", "3GB", PerformanceProfile.LOW, 120, 360),

        // Tecno
        DeviceProfile("Tecno", "Pova 6 Pro 5G", "12GB", PerformanceProfile.ULTRA, 360, 420, true),
        DeviceProfile("Tecno", "Pova 5 Pro", "8GB", PerformanceProfile.HIGH, 240, 392),
        DeviceProfile("Tecno", "Camon 30 Premier", "12GB", PerformanceProfile.ULTRA, 240, 420),
        DeviceProfile("Tecno", "Spark 20 Pro+", "8GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Tecno", "Spark 10C", "4GB", PerformanceProfile.LOW, 180, 360),

        // ASUS ROG
        DeviceProfile("ASUS ROG", "ROG Phone 8 Pro", "12GB", PerformanceProfile.ULTRA, 720, 440, true),
        DeviceProfile("ASUS ROG", "ROG Phone 7 Ultimate", "12GB", PerformanceProfile.ULTRA, 720, 440, true),
        DeviceProfile("ASUS ROG", "ROG Phone 6", "12GB", PerformanceProfile.ULTRA, 720, 420, true),

        // OnePlus
        DeviceProfile("OnePlus", "12R", "12GB", PerformanceProfile.ULTRA, 360, 440, true),
        DeviceProfile("OnePlus", "Nord CE 4 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("OnePlus", "Nord 3 5G", "12GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("OnePlus", "Nord CE 3 Lite", "8GB", PerformanceProfile.BALANCED, 240, 392),

        // Vivo / iQOO
        DeviceProfile("iQOO", "Neo 9 Pro", "12GB", PerformanceProfile.ULTRA, 360, 440, true),
        DeviceProfile("iQOO", "Z9 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("iQOO", "Z7 Pro 5G", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Vivo", "V30 Pro", "12GB", PerformanceProfile.HIGH, 240, 420),
        DeviceProfile("Vivo", "Y200e 5G", "6GB", PerformanceProfile.BALANCED, 180, 392),
        DeviceProfile("Vivo", "Y17s", "4GB", PerformanceProfile.LOW, 180, 360),

        // Motorola
        DeviceProfile("Motorola", "Edge 50 Pro", "12GB", PerformanceProfile.ULTRA, 360, 440, true),
        DeviceProfile("Motorola", "Edge 40 Neo", "8GB", PerformanceProfile.HIGH, 240, 411),
        DeviceProfile("Motorola", "Moto G84 5G", "8GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Motorola", "Moto G54 5G", "8GB", PerformanceProfile.BALANCED, 240, 392),
        DeviceProfile("Motorola", "Moto G14", "4GB", PerformanceProfile.LOW, 180, 360),

        // Google
        DeviceProfile("Google", "Pixel 8 Pro", "12GB", PerformanceProfile.ULTRA, 240, 440, true),
        DeviceProfile("Google", "Pixel 7a", "8GB", PerformanceProfile.HIGH, 240, 411)
    )

    val brands: List<String> = listOf("All") + allDevices.map { it.brand }.distinct().sorted()

    val defaultDevice: DeviceProfile = DeviceProfile(
        brand = "Samsung",
        model = "Galaxy A15",
        ram = "6GB",
        performanceProfile = PerformanceProfile.BALANCED,
        touchSamplingHz = 180,
        defaultDpi = 384
    )

    val ramOptions: List<String> = listOf("2GB", "3GB", "4GB", "6GB", "8GB", "12GB")
}
