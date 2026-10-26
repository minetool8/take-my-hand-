# Take My Hand - EMF Compat (Forge 1.20.1)

Fixes the missing third-person "take my hand" pose when an EMF/CEM player pack is active.

## Why it was broken
Take My Hand writes its pose into the vanilla `PlayerModel` parts at the end of `setupAnim`.
EMF then animates those same parts from the pack's `.jem` expressions and overwrites the pose.
First person worked because Take My Hand renders the first-person arm through its own mixin.

## What this addon does
1. A tiny mixin remembers the `PlayerModel` + entity at the end of `setupAnim`.
2. It registers an `EMFAnimationApi.EMFAnimationHook`. When EMF finishes animating that model
   (`onAnimationEnd`), it calls Take My Hand's own `PlayerAnimation.apply(...)` again, so the
   hand-help pose is layered on top of the pack's result.

No EMF mixins, and Take My Hand's jar is not modified or redistributed (it is "All Rights Reserved").

## Build
Requires JDK 17.
1. Put these jars in `libs/` (compile-only, not bundled):
   - `entity_model_features-3_3_12-1_20_1-forge.jar`
   - `entity_texture_features-7_2_6-1_20_1-forge.jar`
   - `Take_My_Hand-1_0_1-fixed-nooutline.jar`
2. Run `gradlew build` (or `gradle build` if you have no wrapper; run `gradle wrapper` once first).
3. Take `build/libs/handhelp-emf-compat-1.0.0.jar` and put it in your `mods` folder.

## Verify it is working
Offer a hand in-game, then check `latest.log` for:
- `Registered EMF animation hook for Take My Hand.` (at startup)
- `Re-applied Take My Hand pose over an EMF-animated player model.` (when the pose runs)

## Known limits
- Take My Hand applies absolute vanilla-space positions/rotations. If a pack's player rig uses
  different pivots or nested bones for arms/legs/body, the pose may look offset.
- During blend in/out the pose is applied twice (once by Take My Hand, once here), so the
  transition can look slightly snappier. Fully posed frames are identical.
- Tested only by compiling against the real EMF 3.3.12 / ETF 7.2.6 / Take My Hand 1.0.1 jars
  (with stubbed Minecraft/Forge); not run in-game.
