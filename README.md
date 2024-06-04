# ![Snowball Spleef](https://cdn.modrinth.com/data/cached_images/bd4ae5602fffc28590dbca7771ebf5a5f345abe5.png)
A configurable server-side mod that brings back the Tumble functionality of snowballs from Legacy Console Edition.

<div align="center">
  <a href="https://modrinth.com/mod/snowball-spleef"><img src="https://cdn.modrinth.com/data/cached_images/8e05af937c2e2d97c155c0d9c8201edcc1fd1bf2.png" width="150" alt="Modrinth" /></a>
  &nbsp;  &nbsp;  &nbsp;
  <a href="https://modrinth.com/mod/fabric-api"><img src="https://cdn.modrinth.com/data/cached_images/4efd8ed27cfea30edc011e5e504187f23312f593.png" width="159" alt="Fabric API"/></a>
</div>


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
