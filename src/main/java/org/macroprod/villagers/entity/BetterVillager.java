package org.macroprod.villagers.entity;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.entity.HumanEntity;
import org.macroprod.villagers.entity.careers.Career;
import org.macroprod.villagers.entity.careers.Miner;
import org.macroprod.villagers.goal.PathFinderGoalFollowPlayer;
import org.macroprod.villagers.items.Contract;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Created by jasperketelaar on 1/4/17.
 */
public class BetterVillager extends EntityVillager {

    /**
     * Custom career
     */
    private final Career career;

    /**
     * Redefined profession
     */
    private final int profession;

    public BetterVillager(World world) {
        super(world);

        /**
         * Profession ranges from 0-5. Adding new professions messes up client side therefore
         * the best way to add useful villagers is to add new careers to the already existing professions:
         *
         * - 0 = Farmer, Fisherman, Shepherd, Fletcher
         * - 1 = Librarian, Cartographer
         * - 2 = Cleric
         * - 3 = Armorer, Weapon Smith, Tool Smith
         * - 4 = Butcher, Leather Worker
         * - 5 = Nitwit
         */
        this.profession = random.nextInt(1);

        switch (profession) {
            case 0:
                /**
                 * 4 careers already registered (farmer, fisher, shepherd, fletcher) therefore .2 chance to
                 * be a miner.
                 * May need to edit behavior of those.
                 */
                if (random.nextDouble() > 0.1) {
                    this.career = new Miner(this);
                    break;
                }

            default:
                /**
                 * Setting career to null should just use default minecraft behaviour
                 */
                this.career = null;
                break;
        }
        /**
         * We want to modify goals if a custom career has been chosen
         */
        if (career != null) {
            try {
                Field bJ = EntityVillager.class.getDeclaredField("bJ");
                bJ.setAccessible(true);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            career.goals();
        }
    }

    /**
     * Method to clear target and goal selectors
     */
    private void clearSelectors() {
        try {
            Field set1 = PathfinderGoalSelector.class.getDeclaredField("b");
            set1.setAccessible(true);
            set1.set(goalSelector, Sets.newLinkedHashSet());
            set1.set(targetSelector, Sets.newLinkedHashSet());

            Field set2 = PathfinderGoalSelector.class.getDeclaredField("c");
            set2.setAccessible(true);
            set2.set(goalSelector, Sets.newLinkedHashSet());
            set2.set(targetSelector, Sets.newLinkedHashSet());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * May need to modify attributes
     */
    @Override
    public GroupDataEntity a(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity, boolean flag) {
        GroupDataEntity gde = super.a(difficultydamagescaler, groupdataentity, flag);

        return gde;
    }

    /**
     * Gets the profession defined by us for if it's required internally.
     *
     * @return 'custom' profession
     */
    @Override
    public int getProfession() {
        return this.profession;
    }

    /**
     * Remove functionality so profession can't be changed by library
     */
    @Override
    public void setProfession(int profession) {
        if (super.getProfession() != this.profession) {
            super.setProfession(this.profession);
        }
    }

    /**
     * Create a custom name for our custom career if our villager has one.
     *
     * @return
     */
    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        if (career != null) {
            String name = career.getName();
            ChatMessage message = new ChatMessage(name, new Object[0]);
            message.getChatModifier().setChatHoverable(this.bn());
            message.getChatModifier().setInsertion(this.bf());
            ScoreboardTeamBase base = this.aQ();
            if (base != null) {
                message.getChatModifier().setColor(base.m());
            }
            return message;
        } else {
            return super.getScoreboardDisplayName();
        }
    }

    /**
     * Allow our custom careers to have sell custom items
     *
     * @param human
     * @return trade list
     */
    @Override
    public MerchantRecipeList getOffers(EntityHuman human) {
        if (career != null) {
            return career.offers(human);
        } else {
            return super.getOffers(human);
        }
    }

    /**
     * Method that is executed when a recipe is purchased. Used to
     *
     * @param recipe
     */
    @Override
    public void a(MerchantRecipe recipe) {
        if (career != null) {
            if (!career.hasContract()) {
                if (recipe.getBuyItem1().getItem() == Items.EMERALD) {
                    this.riches += recipe.getBuyItem1().getCount();
                }

                if (recipe.getBuyItem3().getItem() == Items.PAPER) {
                    EntityHuman trader = this.getTrader();
                    if (trader != null) {
                        career.setContract(new Contract(this, trader, recipe.getBuyItem3()));
                        this.goalSelector.a(4, new PathFinderGoalFollowPlayer(this, trader.getUniqueID()));
                    }
                }
            }

        } else {
            super.a(recipe);
        }
    }

    /**
     * Override what happens on right click
     * @param human the human clicking
     * @param hand the hand of the human
     */
    @Override
    public boolean a(EntityHuman human, EnumHand hand) {
        if (career != null && human != null && career.hasContract()) {
            return false;
        } else {
            return super.a(human, hand);
        }
    }

}
