/*
 * MegaMek - Copyright (C) 2003, 2004 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */

package megamek.common;

/**
 * Represents a volume of space set aside for carrying vehicles <= 200 tons
 * aboard large spacecraft and mobile structures
 */

public final class SuperHeavyVehicleBay extends Bay {

    /**
     *
     */
    private static final long serialVersionUID = 3490408642054662664L;

    /**
     * The default constructor is only for serialization.
     */
    protected SuperHeavyVehicleBay() {
        totalSpace = 0;
        currentSpace = 0;
    }

    // Public constructors and methods.

    /**
     * Create a space for the given tonnage of troops. For this class, only the
     * weight of the troops (and their equipment) are considered; if you'd like
     * to think that they are stacked like lumber, be my guest.
     *
     * @param space
     *            - The weight of troops (in tons) this space can carry.
     * @param bayNumber
     */
    public SuperHeavyVehicleBay(double space, int doors, int bayNumber) {
        totalSpace = space;
        currentSpace = space;
        this.doors = doors;
        doorsNext = doors;
        this.bayNumber = bayNumber;
        currentdoors = doors;
    }

    /**
     * Determines if this object can accept the given unit. The unit may not be
     * of the appropriate type or there may be no room for the unit.
     *
     * @param unit
     *            - the <code>Entity</code> to be loaded.
     * @return <code>true</code> if the unit can be loaded, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean canLoad(Entity unit) {
        // Assume that we cannot carry the unit.
        boolean result = false;

        // Only tanks or vehicle-mode quadvees equal or less than 200 tons
        // (See IO Battleforce section for the rules that allow converted QVs and LAMs to use other bay types)
        if (((unit instanceof Tank) || (((unit instanceof QuadVee) && (unit.getConversionMode() == QuadVee.CONV_MODE_VEHICLE)))) && (unit.getWeight() <= 200)) {
            result = true;
        }

        // We must have enough space for the new troops.
        // POSSIBLE BUG: we may have to take the Math.ceil() of the weight.
        if (currentSpace < 1) {
            result = false;
        }

        // is the door functional
        if (currentdoors < loadedThisTurn) {
            result = false;
        }
        
        // the bay can't be damaged
        if (damaged == 1) {
        	result = false;
        }

        // Return our result.
        return result;
    }

    @Override
    public String getUnusedString(boolean showrecovery) {
        return "Superheavy Vehicle Bay (" + getCurrentDoors() + " doors) - "
                + String.format("%1$,.0f", currentSpace)
                + (currentSpace > 1 ? " units" : " unit");
    }

    @Override
    public String getType() {
        return "Superheavy Vehicle";
    }

    @Override
    public double getWeight() {
        return totalSpace * 200;
    }

    @Override
    public int getPersonnel(boolean clan) {
        return (int)totalSpace * 15;
    }

    @Override
    public String toString() {
        return "superheavyvehiclebay:" + totalSpace + ":" + doors + ":"+ bayNumber;
    }

    public static TechAdvancement techAdvancement() {
        return new TechAdvancement(TECH_BASE_ALL).setAdvancement(DATE_PS)
                .setTechRating(RATING_A)
                .setAvailability(RATING_C, RATING_C, RATING_C, RATING_C)
                .setStaticTechLevel(SimpleTechLevel.STANDARD);
    }
    
    @Override
    public TechAdvancement getTechAdvancement() {
        return SuperHeavyVehicleBay.techAdvancement();
    }

} // End package class TroopSpace implements Transporter
