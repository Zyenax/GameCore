package net.warvale.core.classes.abilities;

import net.warvale.core.classes.Class;
import net.warvale.core.classes.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

/**
 * Created by Ron on 22/5/2017.
 */
public class AbilityManager implements Listener {

    private ArrayList<Player> cooldown = new ArrayList<>();
    private ArrayList<Player> electroCooldown = new ArrayList<>();
    private ArrayList<Player> fireCooldown = new ArrayList<>();
    private ArrayList<Player> ArcherArrow = new ArrayList<>();
    private ArrayList<Player> freeze = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!(e.getAction() == RIGHT_CLICK_AIR || e.getAction() == RIGHT_CLICK_BLOCK)) return;
        if (cooldown.contains(p)) return;

        Class classCheck = ClassManager.getClassForPlayer(p.getName());

        e.setCancelled(true);

        switch (classCheck.getName()){
            case "Soldier":
                if(!e.getItem().equals(new ItemStack(Material.FIREWORK_CHARGE))) return;
                this.Soldier(p);
                break;



            case "Archer":
                if(!e.getItem().equals(new ItemStack(Material.MAGMA_CREAM))) return;
                this.Archer(p);
                break;


            case "Assassin":
                if (!e.getItem().equals(new ItemStack(Material.SULPHUR))) return;
                this.Assassin(p);
                break;

            case "Miner":
                if (!e.getItem().equals(new ItemStack(Material.IRON_PICKAXE))) return;
                this.Assassin(p);
                break;

            case "Spy":
                if (!e.getItem().equals(new ItemStack(Material.GLASS_BOTTLE))) return;
                this.Spy(p);
                break;

            case "Technician":
                if (!e.getItem().equals(new ItemStack(Material.REDSTONE_TORCH_ON))) return;
                this.Technician(p);
                break;

            case "Musician":
                if (!e.getItem().equals(new ItemStack(Material.RECORD_8))) return;
                this.Musician(p);
                break;

            case "Pyromaniac":
                if (!e.getItem().equals(new ItemStack(Material.FIREBALL))) return;
                this.Pyromaniac(p);
                break;

        }

        cooldown.add(p);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
            public void run() {
                cooldown.remove(p);
                p.sendMessage(ChatColor.GREEN + "You can use your ability now!");
            }
        }, 200);

    }

    @EventHandler
    public void onEntityDamage (EntityDamageByEntityEvent e) {
        LivingEntity entity = (LivingEntity)e.getEntity();
        Player playerEntity = (Player)entity;
        if (entity.getLastDamageCause().getEntity() instanceof Player) { // if a player last damaged the entity
            Player p = (Player)entity.getLastDamageCause().getEntity(); // player who damaged the entity
            if (electroCooldown.contains(p)){
                freeze.add(playerEntity);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
                    public void run() {
                        freeze.remove(entity);
                    }
                }, 100);
            }
            if (fireCooldown.contains(p)) entity.setFireTicks(100);
        }
    }

    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (!freeze.contains(e.getPlayer())) return;
        if (!electroCooldown.contains(e.getPlayer())) return;
        Location from = e.getFrom();
        Location to = e.getTo();
        double x = Math.floor(from.getX());
        double z = Math.floor(from.getZ());
        if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
            x += .5;
            z += .5;
            e.getPlayer().teleport(new Location(from.getWorld(),x,from.getY(),z,from.getYaw(),from.getPitch()));
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
    }

    private void Soldier (Player p) {
        Vector vector = p.getLocation().getDirection();
        vector.setY(0.4);
        p.setVelocity(vector);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
    }
    private void Archer (Player p) {
        Arrow arrow = p.launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(10));
        ArcherArrow.add(p);

    }
    private void Assassin(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 100));
    }

    private void Miner (Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 100));
    }

    private void Spy (Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 100));
    }

    private void Technician (Player p) {
        electroCooldown.add(p);
        p.sendMessage(ChatColor.GREEN + "You can now hit a player to paralyze them!");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
            public void run() {
                electroCooldown.remove(p);
            }
        }, 100);
    }

    private void Musician (Player p) {
        electroCooldown.add(p);
        p.sendMessage(ChatColor.GREEN + "A jukebox has been spawned beneath you!");

        final Block block = p.getLocation().subtract(0, 1, 0).getBlock();
        final Material type = block.getType();

        block.setType(Material.JUKEBOX); //set the block beneat player to jukebox

        // Give player a disc and when the player inserts the disc into the jukebox, give healing in a radius of ten blocks

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
                public void run() {
                    block.setType(type);
                }
        }, 100);
    }

    private void Pyromaniac (Player p) {
        fireCooldown.add(p);
        p.sendMessage(ChatColor.GREEN + "You can now hit a player to light them on fire!");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
            public void run() {
                fireCooldown.remove(p);
            }
        }, 100);
    }


    //         For Archer class.
    @EventHandler
    public void ArcherProjectileHit (ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow)e.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player p = (Player)arrow.getShooter();
        if (!ArcherArrow.contains(p)) return;
        p.getWorld().createExplosion(arrow.getLocation(), 5.0F);
        ArcherArrow.remove(p);
    }




}

