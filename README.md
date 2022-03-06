# GuiCore <img align="right" src="https://user-images.githubusercontent.com/70197204/156937227-2be4d09c-52ec-4f80-a856-159b47c1ba2e.png" height="200" width="200">
Designed as a quick gui creator. I got tired of creating so many guis manually
from yml files. With GuiCore, you can create the template of the Gui in game,
and then edit the specifics in the configuration file.

### Features
- Create gui templates in game
- Command to open gui
- Permission to open gui (optional)
- Background item (filler)
- Click actions (see in example config)
- Custom item cost (for custom shops)
- Vault support
- PlaceholderAPI support
- 1.16 -> 1.18+ support

This plugin can act as basic shops (warps, kits, etc) but it can also
be your entire shop system. However, I'd still recommend using ShopGUIPlus for that.

![gui-gif_1](https://user-images.githubusercontent.com/70197204/156937399-7c03d01a-98e7-459c-8e8e-57c34cd202ce.gif)
![gui-gif_2](https://user-images.githubusercontent.com/70197204/156937406-f1c5b123-e632-4463-b024-10ad83397cf6.gif)

```yaml
# # # # # # # # # # # # # # # #
# EXAMPLE FILE FOR REFERENCE  #
# # # # # # # # # # # # # # # #

# PLUGIN DOES HAVE PLACEHOLDERAPI SUPPORT
# You can put placeholders for the player opening
# the menu in the title, or in the lore! AMAZING RIGHT?


# CANNOT REMOVE THIS PART OF FILE
# Command to open gui
Command: 'examplehelp'
Title: '> Help'
Size: 27

# Everything below is removable
# Don't want a permission, or a
# forced background item? No problem!
Permission: ''

# This displays when a user doesn't
# have access to a particular item below
NoPermissionItem:
  Name: "&4&l! &cPermission denied"
  Lore:
    - "&7&oYou can't view this"
  Material: RED_STAINED_GLASS_PANE

# Backround item for asthetic purposes
BackGroundItem:
  Material: BLACK_STAINED_GLASS_PANE
  Amount: 1

# ITEMS SECTION
# Here's an example of an item
# with every possible feature
# AGAIN, any feature is removable!
Items:
  '0':
    ItemStack:
      Name: "&f&lGeneral Info"
      Lore:
        - "&7This is my server and..."
        - "&7well, it does stuff, that's"
        - "&7really cool I guess, please play..."
        - ""
        - "&7&lCLICK TO PLAY"
      Flags:
        - HIDE_ENCHANTS
      Enchants:
        - DURABILITY:5
      Material: PAPER
      Amount: 1
    # What happens when the item is clicked
    ClickActions:
      PlayerCommands: # What the clicker executes
        - ping
        - msg codepunisher wow man your really cool
      ConsoleCommands: # What the console executes
        - give %player% diamond %
      Sound: BLOCK_NOTE_BLOCK_PLING # Sound that plays
      Permission: "generalinfo.view" # Permission to view and click
      Message: "&a&lYOUR NOW PLAYING MY AMAZING SERVER" # Message after click
      CloseInventory: false # Should inventory close or not
      MoneyCost: 100 # Vault currency

      # This feature is mostly dev based
      # if you have an item stack, and it has
      # a custom namespacekey, that key is
      # checked here. This allows for custom
      # currency implementation.
      #
      # Gem is the key, 100 is the cost
      ItemKey: "Gem:100"
    Slot: 11
  # Simplest form of an item
  '1':
    ItemStack:
      Material: BOOK
      Amount: 1
    Slot: 13
  # Just another example showing
  # you can use any feature you want
  '2':
    ItemStack:
      Name: "&5&lDiscord"
      Lore:
        - "&7&oClick to view discord"
      Material: ENDER_CHEST
    ClickActions:
      Message: "&5&lDiscord Link &flink.omg.discord.blah.blahblahblahblahb.wikihow"
      CloseInventory: true
    Slot: 15

```
