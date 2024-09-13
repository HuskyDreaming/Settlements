package com.huskydreaming.settlements.database.dao;

import com.huskydreaming.huskycore.abstraction.AbstractDao;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Member;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MemberDao extends AbstractDao<Member> {

    public MemberDao(SettlementPlugin plugin) {
        super(plugin);
    }

    @Override
    public int setStatement(PreparedStatement statement, Member member) throws SQLException {
        statement.setString(1, member.getUniqueId().toString());
        statement.setLong(2, member.getSettlementId());
        statement.setLong(3, member.getRoleId());
        statement.setString(4, member.getLastOnline());
        statement.setBoolean(5, member.isAutoClaim());
        return 6;
    }

    @Override
    public Member fromResult(ResultSet result) throws SQLException {
        Member member = new Member();
        member.setId(result.getLong("id"));
        member.setUniqueId(UUID.fromString(result.getString("player_uuid")));
        member.setSettlementId(result.getLong("settlement_id"));
        member.setRoleId(result.getLong("role_id"));
        member.setLastOnline(result.getString("last_online"));
        member.setAutoClaim(result.getBoolean("auto_claim"));
        return member;
    }
}