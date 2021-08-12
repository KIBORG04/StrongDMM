package dmicon

import (
	"sdmm/assets"
	"sdmm/platform"
)

var (
	placeholder *Sprite
)

func initEditorSprites() {
	dmi := &Dmi{
		IconWidth:     32,
		IconHeight:    32,
		TextureWidth:  assets.Editor.Width,
		TextureHeight: assets.Editor.Height,
		Cols:          1,
		Rows:          1,
		Texture:       platform.CreateTexture(assets.Editor.RGBA()),
	}

	placeholder = newDmiSprite(dmi, 0)
}

func SpritePlaceholder() *Sprite {
	if placeholder == nil {
		initEditorSprites()
	}
	return placeholder
}
