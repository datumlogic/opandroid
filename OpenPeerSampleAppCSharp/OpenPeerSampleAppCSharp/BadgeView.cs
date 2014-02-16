using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.Views.Animations;
using Android.Graphics.Drawables.Shapes;

// Code adapted from: https://github.com/jgilfelt/android-viewbadger/blob/master/src/com/readystatesoftware/viewbadger/BadgeView.java

namespace OpenPeerSampleAppCSharp
{
	public class BadgeView : TextView
	{
		public enum Position
		{
			TopLeft,
			TopRight,
			BottomLeft,
			BottomRight,
			Center
		};

		private const int DefaultMarginDip = 5;
		private const int DefaultLRPaddingDip = 5;
		private const int DefaultCornerRadiusDip = 8;
		private const Position DefaultPosition = Position.TopRight;
		private static Color DefaultBadgeColor = Color.ParseColor ("#CCFF0000");
		private static Color DefaultTextColor = Color.White;

		private static Animation FadeIn;
		private static Animation FadeOut;

		private Context context;
		private View target;
		private Position badgePosition = DefaultPosition;
		private int badgeMarginH;
		private int badgeMarginV;
		private Color badgeColor = DefaultBadgeColor;

		private bool isShown = false;

		private ShapeDrawable badgeBackground;

		private int targetTabIndex;

		static BadgeView()
		{
			FadeIn = new AlphaAnimation (0.0f, 1.0f);
			FadeIn.Interpolator = new DecelerateInterpolator ();
			FadeIn.Duration = 200;

			FadeOut = new AlphaAnimation (1.0f, 0.0f);
			FadeOut.Interpolator = new AccelerateDecelerateInterpolator ();
			FadeOut.Duration = 200;
		}

		public BadgeView (Context context) :
			this (context, (IAttributeSet)null, Android.Resource.Attribute.TextViewStyle)
		{
		}

		public BadgeView (Context context, IAttributeSet attrs) :
			this (context, attrs, Android.Resource.Attribute.TextViewStyle)
		{
		}

		public BadgeView (Context context, IAttributeSet attrs, int defStyle) :
			this (context, attrs, defStyle, null, 0)
		{
		}

		public BadgeView (Context context, View target) :
			this (context, null, Android.Resource.Attribute.TextViewStyle, target, 0)
		{
		}

		public BadgeView (Context context, TabWidget target, int index) :
			this(context, null, Android.Resource.Attribute.TextViewStyle, target, 0) {
		}

		public BadgeView (Context context, IAttributeSet attrs, int defStyle, View target, int tabIndex) :
			base(context, attrs, defStyle)
		{
			Initialize(context, target, tabIndex);
		}

		private void Initialize (Context context, View target, int tabIndex)
		{
			this.context = context;
			this.target = target;
			this.targetTabIndex = tabIndex;

			// apply defaults
			badgeMarginH = dipToPixels (DefaultMarginDip);

			Typeface = Typeface.DefaultBold;
			int paddingPixels = dipToPixels (DefaultLRPaddingDip);
			SetPadding (paddingPixels, 0, paddingPixels, 0);
			SetTextColor (DefaultTextColor);

			if (this.target != null) {
				ApplyTo (target);
			} else {
				Show ();
			}
		}

		private void ApplyTo (View target)
		{
			var lp = target.LayoutParameters;
			IViewParent parent = target.Parent;
			FrameLayout container = new FrameLayout (context);

			// borrow some properties from the view which the badge is being applied
			this.Clickable = target.Clickable;
			this.Focusable = target.Focusable;
			this.FocusableInTouchMode = target.FocusableInTouchMode;

			if (target is TabWidget) {
				target = ((TabWidget)target).GetChildTabViewAt (targetTabIndex);
				this.target = target;

				((ViewGroup)target).AddView(
					container,
					new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FillParent, ViewGroup.LayoutParams.FillParent)
				);

				Visibility = ViewStates.Gone;
				container.AddView (this);
			} else {
				ViewGroup group = (ViewGroup)parent;	// will throw an exception if not actually a ViewGroup
				int index = group.IndexOfChild (target);

				group.RemoveView (target);
				group.AddView (container, index, lp);

				container.AddView (target);

				Visibility = ViewStates.Gone;
				container.AddView (this);

				group.Invalidate ();
			}
		}

		public View Target {
			get { return this.target; }
		}

		override public bool IsShown {
			get { return this.isShown; }
		}

		public Position BadgePosition {
			get { return this.badgePosition; }
			set { this.badgePosition = value; }
		}

		public int VeriticalBadgeMargin {
			get { return this.badgeMarginH; }
			set { this.badgeMarginH = value; }
		}

		public int HorizontalBadgeMargin {
			get { return this.badgeMarginV; }
			set { this.badgeMarginV = value; }
		}

		public int BadgeMargin {
			set {
				this.badgeMarginH = value;
				this.badgeMarginV = value;
			}
			get {
				if (badgeMarginH != badgeMarginV) {
					throw new ArgumentException();	// only works if values are in consistent state
				}
				return this.badgeMarginH;
			}
		}

		public Color BadgeBackgroundColor {
			get {return badgeColor;}
			set {
				this.badgeColor = value;
				badgeBackground = DefaultBackground;
			}
		}

		public int increment(int offset)
		{
			string text = this.Text;

			int value = 0;
			if (!String.IsNullOrEmpty(text)) {
				try {
					value = Convert.ToInt32 (text);
				} catch(FormatException) {
				}
			}
			value += offset;
			this.Text = value.ToString ();
			return value;
		}

		public int decrement(int offset)
		{
			return increment (-offset);
		}

		public void Show ()
		{
			Show (false, null);
		}

		public void Show (bool animate)
		{
			Show (animate, FadeIn);
		}

		public void Show (Animation anim)
		{
			Show (anim != null, anim);
		}

		public void Hide ()
		{
			Hide (false, null);
		}

		public void Hide (bool animate)
		{
			Hide (true, FadeOut);
		}

		public void Hide (Animation anim)
		{
			Hide (anim != null, anim);
		}

		public void Toggle ()
		{
			Toggle (false, null, null);
		}

		public void Toggle (bool animate)
		{
			Toggle (animate, FadeIn, FadeOut);
		}

		public void Toggle (Animation animIn, Animation animOut)
		{
			Toggle (true, animIn, animOut);
		}

		private void Show (bool animate, Animation anim)
		{
			if (this.Background == null) {
				if (badgeBackground == null) {
					badgeBackground = this.DefaultBackground;
				}
				this.SetBackgroundDrawable (badgeBackground);
			}

			ApplyLayoutParams ();

			if (animate) {
				this.StartAnimation (anim);
			}

			Visibility = ViewStates.Visible;
			isShown = true;
		}

		private void Hide (bool animate, Animation anim)
		{
			Visibility = ViewStates.Gone;
			if (animate) {
				this.StartAnimation (anim);
			}
			isShown = false;
		}

		private void Toggle(bool animate, Animation animIn, Animation animOut)
		{
			if (isShown) {
				Hide (animate && (animOut != null), animOut);
			} else {
				Show (animate && (animIn != null), animIn);
			}
		}

		private ShapeDrawable DefaultBackground {
			get {
				int r = dipToPixels (DefaultCornerRadiusDip);
				float[] outerR = new float[] { r, r, r, r, r, r, r, r };

				RoundRectShape rr = new RoundRectShape (outerR, null, null);
				ShapeDrawable drawable = new ShapeDrawable (rr);
				drawable.Paint.Color = badgeColor;

				return drawable;
			}
		}

		private void ApplyLayoutParams ()
		{
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WrapContent, ViewGroup.LayoutParams.WrapContent);

			switch (badgePosition) {
			case Position.TopLeft:
				lp.Gravity = GravityFlags.Left | GravityFlags.Top;
				lp.SetMargins(badgeMarginH, badgeMarginV, 0, 0);
				break;
			case Position.TopRight:
				lp.Gravity = GravityFlags.Right | GravityFlags.Top;
				lp.SetMargins(0, badgeMarginV, badgeMarginH, 0);
				break;
			case Position.BottomLeft:
				lp.Gravity = GravityFlags.Left | GravityFlags.Bottom;
				lp.SetMargins(badgeMarginH, 0, 0, badgeMarginV);
				break;
			case Position.BottomRight:
				lp.Gravity = GravityFlags.Right | GravityFlags.Bottom;
				lp.SetMargins(0, 0, badgeMarginH, badgeMarginV);
				break;
			case Position.Center:
				lp.Gravity = GravityFlags.Center;
				lp.SetMargins(0, 0, 0, 0);
				break;
			default:
				break;
			}

			this.LayoutParameters = lp;
		}

		private int dipToPixels(int dip) {
			Android.Content.Res.Resources r = this.Resources;

			float px = TypedValue.ApplyDimension(ComplexUnitType.Dip, dip, r.DisplayMetrics);
			return (int) px;
		}
	}
}

