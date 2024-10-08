package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.implementations.RepositoryImpl;
import com.huskydreaming.huskycore.interfaces.Repository;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.SqlType;
import com.huskydreaming.settlements.database.dao.ClaimDao;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClaimServiceImpl implements ClaimService {

    private final ClaimDao claimDao;
    private final Repository<Claim> claimRepository;

    public ClaimServiceImpl(SettlementPlugin plugin) {
        this.claimDao = new ClaimDao(plugin);
        this.claimRepository = new RepositoryImpl<>();
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        claimDao.bulkImport(SqlType.CLAIM, claimRepository::bulkAdd);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        claimDao.bulkUpdate(SqlType.CLAIM, claimRepository.values());
    }

    @Override
    public Claim createClaim(Chunk chunk) {
        Claim claim = new Claim();
        claim.setWorldUID(chunk.getWorld().getUID());
        claim.setX(chunk.getX());
        claim.setZ(chunk.getZ());

        return claim;
    }

    @Override
    public void addClaim(Claim claim) {
        claimRepository.add(claim);
    }

    @Override
    public void addClaim(Settlement settlement, Chunk chunk, Runnable runnable) {
        Claim claim = new Claim();
        claim.setSettlementId(settlement.getId());
        claim.setWorldUID(chunk.getWorld().getUID());
        claim.setX(chunk.getX());
        claim.setZ(chunk.getZ());

        claimDao.insert(claim).queue(i -> {
            claim.setId(i);
            claimRepository.add(claim);
            runnable.run();
        });
    }


    @Override
    public void removeClaim(Claim claim, Runnable runnable) {
        claimDao.delete(claim).queue(i -> {
            claimRepository.remove(claim);
            runnable.run();
        });
    }

    @Override
    public void clean(Settlement settlement) {
        Set<Long> claimIds = claimRepository.entries().stream()
                .filter(e -> e.getValue().getSettlementId() == settlement.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        claimDao.bulkDelete(SqlType.CLAIM, claimIds);
        claimRepository.keys().removeAll(claimIds);
    }

    @Override
    public boolean isClaim(Chunk chunk) {
        return claimRepository.values().stream().anyMatch(c ->
                c.getWorldUID() == chunk.getWorld().getUID() &&
                c.getX() == chunk.getX() &&
                c.getZ() == chunk.getZ());
    }

    @Override
    public Claim getClaim(Settlement settlement, Chunk chunk) {
        Predicate<Claim> claimPredicate = (c ->
                c.getSettlementId() == settlement.getId() &&
                c.getWorldUID().equals(chunk.getWorld().getUID()) &&
                c.getX() == chunk.getX() &&
                c.getZ() == chunk.getZ());

        return claimRepository.values().stream().filter(claimPredicate).findFirst().orElse(null);
    }

    @Override
    public Claim getClaim(Chunk chunk) {
        Predicate<Claim> claimPredicate = (c ->
                c.getWorldUID().equals(chunk.getWorld().getUID()) &&
                c.getX() == chunk.getX() &&
                c.getZ() == chunk.getZ());

        return claimRepository.values().stream().filter(claimPredicate).findFirst().orElse(null);
    }

    @Override
    public Set<Claim> getClaims(long settlementId) {
        return claimRepository.values().stream()
                .filter(c -> c.getSettlementId() == settlementId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Claim> getClaims(Settlement settlement) {
        return claimRepository.values().stream()
                .filter(c -> c.getSettlementId() == settlement.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public int getCount() {
        return claimRepository.size();
    }

    @Override
    public LinkedHashMap<Long, Long> getTop(int limit) {
        return claimRepository.values().stream()
                .collect(Collectors.groupingBy(Claim::getSettlementId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public boolean isAdjacent(Settlement settlement, Chunk chunk) {
        boolean adjacent = false;
        for (Claim data : getClaims(settlement)) {
            if (Util.areAdjacentChunks(chunk, data.toChunk())) {
                adjacent = true;
            }
        }
        return adjacent;
    }

    @Override
    public boolean isAdjacentToOtherClaim(Settlement settlement, Chunk chunk) {
        World world = chunk.getWorld();
        boolean adjacent = false;

        for (BlockFace blockFace : Util.chunkSteps) {
            int modX = blockFace.getModX();
            int modZ = blockFace.getModZ();
            Chunk adjacentChunk = world.getChunkAt(chunk.getX() + modX, chunk.getZ() + modZ);
            if (!isClaim(adjacentChunk)) continue;

            Claim adjacentClaim = getClaim(adjacentChunk);
            if(adjacentClaim.getSettlementId() != settlement.getId()) adjacent = true;
        }
        return adjacent;
    }

    @Override
    public boolean isAdjacentToExistingClaim(Chunk chunk) {
        World world = chunk.getWorld();
        boolean adjacent = false;

        for (BlockFace blockFace : Util.chunkSteps) {
            int modX = blockFace.getModX();
            int modZ = blockFace.getModZ();
            Chunk adjacentChunk = world.getChunkAt(chunk.getX() + modX, chunk.getZ() + modZ);
            if (isClaim(adjacentChunk)) adjacent = true;
        }
        return adjacent;
    }

    @Override
    public ClaimDao getDao() {
        return claimDao;
    }
}