1. there's a scale button which changes the "scale" from 10 to 6.2 and vice versa, so as to implement a numerical change in the dist. units
2. there's a second button that changes the color distribution in the prog bar till stage 10, it also keeps on increasing the dist_covered, curr and the next stage till infinity
3. as mentioned in the prev point, the color dist. changes in the prog bar to represent stage transition
4. also, the stage_left var indicates the distance till stage 10 in stages inclusive of [1,10], it stops at 0 after the user's supposed to shift to the lazy column, where there's no need to establish an upper limit, thus reducing its meaning to moot
5. the lazy list is originally restricted to 10 stages but can increase should the user decide to move further or whatever
6. there's 2 main functions, top_blue for, as the name suggests, the bg color and title at the top of the app, and journey_app for the rest
