package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface MemberService extends ServiceInterface {

    boolean hasSettlement(OfflinePlayer offlinePlayer);

    void add(OfflinePlayer offlinePlayer, Settlement settlement);

    void remove(OfflinePlayer offlinePlayer);

    void clean(Settlement settlement);

    Member getCitizen(OfflinePlayer offlinePlayer);

    List<Member> getMembers(Settlement settlement);

    List<OfflinePlayer> getOfflinePlayers(Settlement settlement);
}
