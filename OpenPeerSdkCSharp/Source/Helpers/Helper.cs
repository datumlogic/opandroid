using System;
using System.Collections.Generic;
using System.Linq;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		public class Helper
		{
			private Helper ()	{}

			public static bool IsNullOrEmpty<T>(IEnumerable<T> enumerable) {
				return enumerable == null || !enumerable.Any();
			}
		}
	}
}
