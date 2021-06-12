package com.vczero98.game.world;


public class Block {
    private BlockType type;
    private ItemType itemType = ItemType.NONE;

    public Block(BlockType type) {
        this.type = type;
    }

    public BlockType getType() {
        return type;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setType(BlockType type) {
        this.type = type;
    }
    
    public String toString() {
        return "Type: " + type + ", Item: " + itemType;
    }
}
