# Выдрёнок BuyTown

Изображение создано встроенным инструментом imagegen. Прозрачность исходного PNG сохранена.

- Ассет: `app/src/main/res/drawable-nodpi/otter_shopping_run.png`.
- Размер: 1536 × 1024, 4 столбца × 2 строки, восемь кадров.
- Компонент: `presentation/auth/ShoppingOtterLogo.kt`.
- Цикл: 720 мс; движение остаётся локальным состоянием интерфейса и останавливается, когда экран перестаёт быть видимым. Скорость учитывает системный масштаб анимации Compose.
- Показан на авторизации и регистрации вместо текстового логотипа. Описание для TalkBack статическое, без объявления каждой смены кадра.

## Финальный промпт

Use case: logo-brand. Asset type: transparent animation sprite sheet for the BuyTown Android login mascot, replacing the plain text logo. Primary request: an exceptionally charming baby river otter joyfully RUNNING toward the right with shopping bags, an original polished mascot illustration with a warm personality. Rounded cocoa-brown body, cream muzzle and tummy, tiny round ears, shiny dark eyes, little whiskers, broad tapered otter tail, expressive happy smile. Two paper shopping bags, one teal and one deep blue to match an existing blue/teal shopping app. Style: premium crisp hand-drawn vector-like mascot with confident clean dark brown outlines, subtle soft shading, simplified shapes readable at 140dp. NOT photorealistic. Composition: EXACTLY FOUR equal columns and TWO equal rows, EIGHT evenly spaced animation frames in reading order; a seamless 8-pose run cycle: contact, down, passing, up, opposite contact, down, passing, up. All frames show the SAME otter character from the same right-facing 3/4 side view at IDENTICAL SCALE and with the SAME center/body anchor; only leg positions, body bounce, tail and bag swing change. Alternate extended and tucked legs convincingly. Whole character and both bags fit fully inside each cell with generous transparent padding, no overlaps across cells. Each cell same size. Transparent background and transparent gutters, NO solid background, NO checkerboard drawn into the image. Frame 8 flows back to frame 1. No ground, no shadows outside character, no words, no letters, no numbering, no borders, no watermark, no decorative scene. Landscape sheet 3:2 aspect ratio if possible.
