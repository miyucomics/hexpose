# Hexpose

## 2.0.0
- new major version due to breaking API changes
- added patterns to flick Hexpose iotas into MoreIotas iotas and vice versa
- added patterns to get mob anger
- added patterns to get mob attacker and attacked time
- added spell to use display iotas to set name and lore of items
- changed display iotas to be recursive
  - you are now responsible for managing children and styles
  - Minecraft `Text`, called `Component` in Mojmap, is a recursive data model. A `Text` has content and styling information, but it can also contain a number of children, which themselves contain children. Styles are inherited parent to child if the child does not specify them
  - in 1.0.0, text iotas would automatically handle all this information for you. Create some text, style the entire thing, and concatenate them to mix and match styles
  - however, this leads to a lot of unintuitive behavior that would simply be unreasonable for the developer to handle. Is this piece of green text a single text, or actually `Text` with a child for every letter, or actually split at some random ratio? All of those would not equal each other
  - the only solution that I can execute would be to dynamically flatten and simplify `Text` on the fly. However, that raises its own issues if I invisibly alter the composition of `Text` on the fly. Thus, the solution I have determined is to release a breaking API update to give that responsibility entirely to the user
- fixed Offer Purification sampling count from incorrect position
- renamed text iotas to display iotas to avoid confusion with strings
- removed Newspaper Reflection
  - no other pattern does the same "retroactive" effect and I think I'd be cool if the player had to handle it
- removed piercing raycasts
  - since it's all just math, I just think it'd be neat if the player had to do it instead
  - there will be replacement patterns that return the list of things to check and the direction so it's not a complete loss
  - in fact, it can be thought of as a benefit due to increased flexibility when it is the player that has to handle it

## 1.0.0
- initial release