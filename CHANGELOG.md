# Hexpose

## 3.0.0
- added MoreIotas as a required dependency
  - this removes a competing standard, although there still will be patterns that appear to do similar things, like the rename spell. With the power of display iotas, you can get a lot more display customization and the default MoreIotas spell does that support that, and it would just be impractical to mixin in and duct tape it into working.
  - for other patterns like the time getting one, it is kept simply because I think mine is prettier and more memorable, and I'd prefer not to break hexes.
- removed identifier iotas
- removed item stack iotas

## 2.0.0
- new major version due to breaking API changes
- added ability to get the book from lectern
- added patterns to flick Hexpose iotas into MoreIotas iotas and vice versa
- added ability to read from item stack iotas
- added patterns to read tags from entities, blocks, and items
- added patterns to get mob anger
- added pattern to test for slime chunks
- added pattern to get whether chunk is loaded
- added pattern to get shooter of entity
- added pattern to get whether an entity is considered a monster
- added some nice block scrying patterns
  - whether it is a type of air
  - replaceability
  - slipperiness
- added patterns to get mob attacker and attacked time
- added basic free spells. It seems unreasonable that a mod that has a pattern to get item frame rotation would need an accessory mod to set item frame rotation for example, or for a mod that has display iotas and the ability to read the name of an item to not have a spell to set those things
  - added spell to use display iotas to set name and lore of items
  - added spell to set item frame rotation
- changed display iotas to be recursive
  - you are now responsible for managing children and styles
  - Minecraft `Text`, called `Component` in Mojmap, is a recursive data model. A `Text` has content and styling information, but it can also contain a number of children, which themselves contain children. Styles are inherited parent to child if the child does not specify them
  - in 1.0.0, text iotas would automatically handle all this information for you. Create some text, style the entire thing, and concatenate them to mix and match styles
  - however, this leads to a lot of unintuitive behavior that would simply be unreasonable for the developer to handle. Is this piece of green text a single text, or actually `Text` with a child for every letter, or actually split at some random ratio? All of those would not equal each other
  - the only solution that I can execute would be to dynamically flatten and simplify `Text` on the fly. However, that raises its own issues if I invisibly alter the composition of `Text` on the fly. Thus, the solution I have determined is to release a breaking API update to give that responsibility entirely to the user
- changed Potential Purification to accept entities and blocks, useful for interop
- fixed Offer Purification taking the count parameter from incorrect position
- removed piercing raycasts since it's all just math. I just think it'd be neat if the player had to do it instead
- renamed text iotas to display iotas to avoid confusion with strings
- reworked Newspaper Reflection
  - no other pattern does the same "retroactive" effect and I think I'd be cool if the player had to handle it
  - now there is News Disintegration in case you do want to dig back in time, and it won't blow up your stack with a giant list

## 1.0.0
- initial release
