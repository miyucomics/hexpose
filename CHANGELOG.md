# Hexpose

## 2.0.0
- new major version due to changes in ~~text~~ display iotas
- display iotas are now recursive; you are responsible for managing children and styles
  - Minecraft `Text`, called `Component` in Mojmap, is a recursive data model. A `Text` has content and styling information, but it can also contain a number of children, which themselves contain children. Styles are inherited parent to child if the child does not specify them.
  - in 1.0.0, text iotas would automatically handle all this information for you. Create some text, style the entire thing, and concatenate them to mix and match styles.
  - however, this leads to a lot of unintuitive behavior that would simply be unreasonable for the developer to handle. Is this piece of green text a single text, or actually `Text` with a child for every letter, or actually split at some random ratio? All of those would not equal each other.
  - the only solution that I can execute would be to dynamically flatten and simplify `Text` on the fly. However, that raises its own issues if I invisibly alter the composition of `Text` on the fly. Thus, the solution I have determined is to release a breaking API update to give that responsibility entirely to the user. Thus, display iotas were born.
- renamed text iotas to display iotas to avoid confusion with strings
- fixed Offer Purification sampling count from incorrect position

## 1.0.0
- initial release