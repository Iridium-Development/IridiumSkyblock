CREATE UNIQUE INDEX island_bank_id_island_id_uindex ON island_bank (id, bank_item, island_id);
CREATE UNIQUE INDEX island_bans_id_bannedUser_island_id_uindex ON island_bans (id, bannedUser, island_id);
CREATE UNIQUE INDEX island_blocks_id_block_island_id_uindex ON island_blocks (id, block, island_id);
CREATE UNIQUE INDEX island_booster_id_booster_island_id_uindex ON island_booster (id, booster, island_id);
CREATE UNIQUE INDEX island_invites_id_user_island_id_uindex ON island_invites (id, user, island_id);
CREATE UNIQUE INDEX island_logs_id_island_id_uindex ON island_logs (id, island_id);
CREATE UNIQUE INDEX island_mission_id_mission_name_island_id_uindex ON island_mission (id, mission_name, island_id);
CREATE UNIQUE INDEX island_permissions_id_island_id_uindex ON island_permissions (id, island_id);
CREATE UNIQUE INDEX island_rewards_id_island_id_uindex ON island_rewards (id, island_id);
CREATE UNIQUE INDEX island_spawners_id_spawner_type_island_id_uindex ON island_spawners (id, spawner_type, island_id);
CREATE UNIQUE INDEX island_trusted_id_user_island_id_uindex ON island_trusted (id, user, island_id);
CREATE UNIQUE INDEX island_upgrade_id_upgrade_island_id_uindex ON island_upgrade (id, upgrade, island_id);
CREATE UNIQUE INDEX island_warps_id_name_island_id_uindex ON island_warps (id, name, island_id);
