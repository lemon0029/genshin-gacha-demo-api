package me.xuqu;

import lombok.RequiredArgsConstructor;
import me.xuqu.model.entity.gacha.GenshinGachaPoolItem;
import me.xuqu.repository.GenshinCharacterRepository;
import me.xuqu.repository.GenshinGachaPoolItemRepository;
import me.xuqu.repository.GenshinWeaponRepository;
import me.xuqu.schedule.FetchGenshinGachaPoolData;
import me.xuqu.schedule.FetchGenshinItemData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class GenshinGachaDemoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenshinGachaDemoApiApplication.class, args);
    }

    private final GenshinWeaponRepository genshinWeaponRepository;
    private final GenshinCharacterRepository genshinCharacterRepository;
    private final GenshinGachaPoolItemRepository genshinGachaPoolItemRepository;

    private final FetchGenshinItemData fetchGenshinItemData;
    private final FetchGenshinGachaPoolData fetchGenshinGachaPoolData;

    //    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            for (GenshinGachaPoolItem genshinGachaPoolItem : genshinGachaPoolItemRepository.findAll()) {
                Long id;
                if (genshinGachaPoolItem.getType().equals("武器")) {
                    id = genshinWeaponRepository.findByName(genshinGachaPoolItem.getName()).orElseThrow().getId();
                } else {
                    id = genshinCharacterRepository.findByName(genshinGachaPoolItem.getName()).orElseThrow().getId();
                }
                genshinGachaPoolItem.setItemId(id);
                genshinGachaPoolItemRepository.save(genshinGachaPoolItem);
            }

        };
    }

    @Bean
    public CommandLineRunner fetchItem() {
        return args -> {
            fetchGenshinItemData.fetWeapons();
            fetchGenshinItemData.fetchCharacters();
            fetchGenshinItemData.fetchCharacterFix();
        };
    }

    @Bean
    public CommandLineRunner fetchPool() {
        return args -> {
            fetchGenshinGachaPoolData.fetchGachaList();
            fetchGenshinGachaPoolData.fetchGachaPoolInfo();
        };
    }
}
