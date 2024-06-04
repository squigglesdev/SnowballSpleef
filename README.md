# ![Snowball Spleef](https://cdn.modrinth.com/data/cached_images/bd4ae5602fffc28590dbca7771ebf5a5f345abe5.png)
Configurable server-side mod that brings back the Tumble functionality of snowballs from Legacy Console Edition.

## Features

### Configurable block destroy list

Server admins can, using the `/snowball` command, specify a list of blocks that can be destroyed by snowballs:
```mcfunction
snowball add stone
```
```
>> Added block stone to breakable list
```
<br />

```mcfunction
snowball remove stone
```
```
>> Removed block stone from breakable list
```
<br />

```mcfunction
snowball list
```
```
>> The current breakable blocks are: stone, grass_block, ice
```

### Ignite TNT

Server admins can also specify if snowballs ignite TNT, similar to the Legacy Console Edition minigame:

```mcfunction
snowball ignitesTNT true
```
```
>> SnowballsIgniteTNT set to true
```
<br />

```mcfunction
snowball ignitesTNT false
```
```
>> SnowballsIgniteTNT set to false
```

### Hurt Players

Snowballs now deal a small amount of damage and knockback to players, similar to the Legacy Console Edition minigame.
