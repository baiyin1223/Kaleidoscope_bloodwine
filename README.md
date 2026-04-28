# Kaleidoscope Bloodwine

## Introduction

Kaleidoscope Bloodwine is an addon mod for **KaleidoscopeTavern**, bringing fantasy-style blood wine experience to Minecraft.

## Project Info

- **Mod Name**: Kaleidoscope Bloodwine
- **Minecraft Version**: Forge 1.20.1|neoforge 1.21.1
- **Author**: baiyin_1223
- **License**: MIT

## Features

- Provides **11** fantasy-style blood wines
- Each wine has its own model and unique effects

## Compatibility

### Vampirism Integration

- If Vampirism is loaded, some wines will be brewed using Vampirism mod materials and use **vampirism:blood**
- Blood wines can restore blood value for vampire players and provide additional buffs

### Standalone

- This mod only requires KaleidoscopeTavern as a dependency and can run without Vampirism. In this case, a new blood fluid will be created instead of using vampirism:blood.

### Create: Gears and Tavern (CGT) & Create Integration

- Supports [Create: Gears and Tavern](https://github.com/yision1/GearsandTavern) and the Create mod
- Blood wines can be transported through Create's fluid pipe system
- Supports Create emptying/filling recipes for all blood wines at every brew level
- Barrels display virtual blood wine fluids during brewing, compatible with Create's mechanical pump extraction

## Dependencies

| Mod | Note |
|-----|------|
| KaleidoscopeTavern | **Required** - Main mod |
| Create: Gears and Tavern | *Optional* - Enables Create fluid automation |
| Create | *Optional* - Required by CGT |
| Vampirism | *Optional* -Enables Vampirism support |

## License

This project is licensed under the MIT License.